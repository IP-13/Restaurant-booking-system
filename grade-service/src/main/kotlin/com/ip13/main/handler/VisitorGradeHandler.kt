package com.ip13.main.handler

import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFound
import com.ip13.main.model.dto.request.VisitorGradeRequest
import com.ip13.main.model.entity.User
import com.ip13.main.model.entity.VisitorGrade
import com.ip13.main.repository.VisitorGradeRepository
import com.ip13.main.util.getLogger
import com.ip13.main.webClient.blackListService.BlackListServiceWebClient
import com.ip13.main.webClient.blackListService.dto.BlackListRequest
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component
class VisitorGradeHandler(
    private val restaurantServiceWebClient: RestaurantServiceWebClient,
    private val blackListServiceWebClient: BlackListServiceWebClient,
    private val userHandler: UserHandler,
    private val visitorGradeRepository: VisitorGradeRepository,
) {
    private val log = getLogger(javaClass)

    fun gradeVisitor(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in grade visitor method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                request
                    .bodyToMono(VisitorGradeRequest::class.java)
                    .flatMap { req ->
                        restaurantServiceWebClient
                            .getTableReserveTicket(
                                req.tableReserveTicketId,
                                request.headers().header(HttpHeaders.AUTHORIZATION).first()
                            )
                            .log()
                            .flatMap { ticket ->
                                // save manager in no exists
                                request.principal().flatMap { principal ->
                                    userHandler.findByUsername(principal.name)
                                        .flatMap {
                                            log.debug("manager found")
                                            Mono.just(it)
                                        }
                                        .switchIfEmpty(
                                            userHandler.save(
                                                User(
                                                    username = principal.name,
                                                    numOfGrades = 0,
                                                    sumOfGrades = 0,
                                                )
                                            )
                                        )
                                }.flatMap {
                                    log.debug("Checking if user exists")
                                    userHandler.findByUsername(ticket.username)
                                        .flatMap {
                                            log.debug("User found. Adding grade")
                                            userHandler.addGrade(ticket.username, req.grade)
                                        }
                                        .switchIfEmpty(
                                            userHandler.save(
                                                User(
                                                    username = ticket.username,
                                                    numOfGrades = 1,
                                                    sumOfGrades = req.grade
                                                )
                                            ).flatMap {
                                                log.debug("User not found. Saving user")
                                                Mono.just(it.sumOfGrades)
                                            }
                                        )
                                        .flatMap {
                                            log.debug("Saving visitor grade to db")
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
                                            }.flatMap {
                                                log.debug("Getting updated grade from db")
                                                userHandler.getGrade(ticket.username).flatMap { grade ->
                                                    if (grade < 3.0) {
                                                        log.debug("Adding ${ticket.username} to black list")
                                                        blackListServiceWebClient.addToBlackList(
                                                            BlackListRequest(
                                                                username = ticket.username,
                                                                fromDate = LocalDateTime.now(),
                                                                tillDate = LocalDateTime.now().plusMonths(3),
                                                                reason = "Average grade less than 3.0"
                                                            ),
                                                            request.headers().header(HttpHeaders.AUTHORIZATION).first()
                                                        )
                                                    }
                                                    Mono.just(grade)
                                                }
                                            }
                                        }
                                }
                            }
                            .switchIfEmpty(Mono.error(TableReserveTicketNotFound()))
                            .log()
                    }
                    .log(),
                Float::class.java
            )
    }

    fun getGrade(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in grade visitor method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                userHandler.getGrade(request.pathVariable("username")),
                Float::class.java
            )
    }

}