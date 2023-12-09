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
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import com.ip13.main.webClient.restaurantService.dto.TableReserveTicket
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
    private val restaurantServiceWebClient: RestaurantServiceWebClient,
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
                    .flatMap { req ->
                        restaurantServiceWebClient
                            .getTableReserveTicket(
                                req.tableReserveTicketId,
                                request.headers().header(HttpHeaders.AUTHORIZATION).first()
                            )
                            .log()
                            .flatMap { ticket ->
                                log.debug("Before user repository")
                                userRepository.findByUsername(ticket.username)
                                    .flatMap {
                                        log.debug("user found")
                                        request.principal().flatMap { principal ->
                                            visitorGradeRepository.save(
                                                VisitorGrade(
                                                    managerName = principal.name,
                                                    tableReserveTicketId = req.tableReserveTicketId,
                                                    username = ticket.username,
                                                    grade = req.grade,
                                                    comment = req.comment,
                                                )
                                            ).log()
                                        }
                                        userRepository.addGrade(ticket.username, req.grade)
                                    }
                                    .log()
                                    .switchIfEmpty(
                                        userRepository.save(
                                            User(
                                                username = ticket.username,
                                                numOfGrades = 1,
                                                sumOfGrades = req.grade
                                            )
                                        ).flatMap {
                                            log.debug("user not found")
                                            request.principal().flatMap { principal ->
                                                log.debug("saving visitor grade")
                                                visitorGradeRepository.save(
                                                    VisitorGrade(
                                                        managerName = principal.name, // add manager as well
                                                        tableReserveTicketId = req.tableReserveTicketId,
                                                        username = ticket.username,
                                                        grade = req.grade,
                                                        comment = req.comment,
                                                    )
                                                ).log()
                                            }
                                            log.debug("num of grades: {}", it.numOfGrades)
                                            log.debug("sum of grades: {}", it.sumOfGrades)
                                            Mono.just(it.sumOfGrades)
                                        }
                                    ).log().flatMap {
                                        userRepository.getGrade(ticket.username)
                                    }
                            }
                            .log()
                            .switchIfEmpty(Mono.error(TableReserveTicketNotFound())).log()
                    }
                    .log(),
                Float::class.java
            )
    }
}