package com.ip13.main.handler

import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFound
import com.ip13.main.model.dto.request.RestaurantGradeRequest
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.entity.User
import com.ip13.main.repository.RestaurantGradeRepository
import com.ip13.main.util.getLogger
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class RestaurantGradeHandler(
    private val restaurantGradeRepository: RestaurantGradeRepository,
    private val restaurantHandler: RestaurantHandler,
    private val userHandler: UserHandler,
    private val restaurantServiceWebClient: RestaurantServiceWebClient,
) {
    private val log = getLogger(javaClass)

    fun gradeRestaurant(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in grade restaurant method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                request
                    .bodyToMono(RestaurantGradeRequest::class.java)
                    .flatMap { req ->
                        restaurantServiceWebClient
                            .getTableReserveTicket(
                                req.tableReserveTicketId,
                                request.headers().header(HttpHeaders.AUTHORIZATION).first()
                            )
                            .log()
                            .flatMap { ticket ->
                                restaurantHandler.findByRestaurantId(ticket.restaurantId)
                                    .flatMap { restaurant ->
                                        log.debug("restaurant found")
                                        restaurantHandler.addGrade(restaurant.restaurantId, req.grade)
                                        Mono.just(restaurant)
                                    }
                                    .switchIfEmpty(
                                        restaurantHandler.save(
                                            Restaurant(
                                                restaurantId = ticket.restaurantId,
                                                numOfGrades = 1,
                                                sumOfGrades = req.grade,
                                            )
                                        )
                                    )
                                    .flatMap {
                                        userHandler.findByUsername(ticket.username)
                                            .switchIfEmpty(
                                                userHandler.save(
                                                    User(
                                                        username = ticket.username,
                                                        numOfGrades = 0,
                                                        sumOfGrades = 0,
                                                    )
                                                )
                                            ).flatMap {
                                                restaurantGradeRepository.save(
                                                    RestaurantGrade(
                                                        username = ticket.username,
                                                        tableReserveTicketId = req.tableReserveTicketId,
                                                        restaurantId = ticket.restaurantId,
                                                        grade = req.grade,
                                                        comment = req.comment,
                                                    )
                                                )
                                            }
                                    }
                                    .flatMap {
                                        log.debug("Getting updated grade from db")
                                        restaurantHandler.getGrade(ticket.restaurantId)
                                    }
                            }
                            .switchIfEmpty(Mono.error(TableReserveTicketNotFound()))
                            .log()
                    },
                Float::class.java
            )
    }

    fun getGrade(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in get grade restaurant method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                restaurantHandler.getGrade(request.pathVariable("id").toInt()),
                Float::class.java
            )
    }
}