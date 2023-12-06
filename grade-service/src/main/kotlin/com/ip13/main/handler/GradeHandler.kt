package com.ip13.main.handler

import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFound
import com.ip13.main.model.dto.request.RestaurantGradeRequest
import com.ip13.main.model.entity.User
import com.ip13.main.model.entity.VisitorGrade
import com.ip13.main.repository.RestaurantGradeRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.repository.UserRepository
import com.ip13.main.repository.VisitorGradeRepository
import com.ip13.main.util.getLogger
import com.ip13.main.webClient.dto.TableReserveTicket
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class GradeHandler(
    private val userServiceClient: WebClient,
    private val restaurantServiceClient: WebClient,
    private val restaurantGradeRepository: RestaurantGradeRepository,
    private val restaurantRepository: RestaurantRepository,
    private val userRepository: UserRepository,
    private val visitorGradeRepository: VisitorGradeRepository,
) {
    private val log = getLogger(javaClass)

    fun gradeUser(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in grade user method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                request
                    .bodyToMono(RestaurantGradeRequest::class.java)
                    .log()
                    .map { req ->
                        restaurantServiceClient
                            .get()
                            .uri("/reservation/table-reserve-ticket/${req.tableReserveTicketId}")
                            .accept(MediaType.APPLICATION_JSON)
                            .header(
                                HttpHeaders.AUTHORIZATION,
                                request.headers().header(HttpHeaders.AUTHORIZATION).first()
                            )
                            .retrieve()
                            .bodyToMono(TableReserveTicket::class.java)
                            .log()
                            .map { ticket ->
                                visitorGradeRepository.save(
                                    VisitorGrade(
                                        managerName = "manager",
                                        tableReserveTicketId = req.tableReserveTicketId,
                                        username = ticket.username,
                                        grade = req.grade,
                                        comment = req.comment,
                                    )
                                ).log().subscribe()
                                userRepository.findById(ticket.username)
                                    .map {
                                        userRepository.addGrade(ticket.username, req.grade)
                                    }.log()
                                    .switchIfEmpty(
                                        userRepository.save(
                                            User(
                                                username = ticket.username,
                                                numOfGrades = 1,
                                                sumOfGrades = req.grade
                                            )
                                        ).map {
                                            log.debug("added user to db")
                                            log.debug("sum of grades: ${it.sumOfGrades}")
                                            it.sumOfGrades
                                        }
                                    ).log()
                                userRepository.getGrade(ticket.username)
                            }.log()
                            .switchIfEmpty(Mono.error(TableReserveTicketNotFound())).log()
                    }
                    .log(),
                Float::class.java
            )
    }
}