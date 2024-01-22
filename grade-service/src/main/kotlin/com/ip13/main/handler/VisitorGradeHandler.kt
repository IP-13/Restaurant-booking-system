package com.ip13.main.handler

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.NotFoundException
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
import org.springframework.http.HttpStatus
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

    @Suppress("kotlin:S3776")
    fun gradeVisitor(request: ServerRequest): Mono<ServerResponse> {
        log.debug("in grade visitor method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                request
                    .bodyToMono(VisitorGradeRequest::class.java)
                    .flatMap { req ->
                        visitorGradeRepository.existsByTableReserveTicketId(req.tableReserveTicketId).flatMap { b ->
                            if (b == true) {
                                Mono.error(
                                    CommonException(
                                        "ticket with id ${req.tableReserveTicketId} already graded",
                                        HttpStatus.BAD_REQUEST
                                    )
                                )
                            } else {
                                restaurantServiceWebClient.getTableReserveTicket(
                                    req.tableReserveTicketId,
                                    request.headers().header(HttpHeaders.AUTHORIZATION).first()
                                ).log().flatMap { ticket ->
                                    // save manager in no exists
                                    request.principal().flatMap { principal ->
                                        if (ticket.managerName != principal.name) {
                                            Mono.error(
                                                CommonException(
                                                    "Attempt to grade ticket which doesn't belong to you",
                                                    HttpStatus.METHOD_NOT_ALLOWED
                                                )
                                            )
                                        } else {
                                            userHandler.findByUsername(principal.name)
                                                .flatMap {
                                                    log.debug("manager found")
                                                    Mono.just(it)
                                                }
                                                .switchIfEmpty(
                                                    Mono.error(
                                                        NotFoundException(
                                                            message = "Manager ${principal.name} not found"
                                                        )
                                                    )
                                                )
                                        }
                                    }.flatMap {
                                        log.debug("Checking if user exists")
                                        userHandler.findByUsername(ticket.username)
                                            .flatMap {
                                                log.debug("User found. Adding grade")
                                                userHandler.addGrade(ticket.username, req.grade)
                                            }
                                            .switchIfEmpty(
                                                Mono.error(
                                                    NotFoundException(
                                                        message = "User ${ticket.username} not found"
                                                    )
                                                )
                                            )
                                    }.flatMap { grade ->
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
                                            ).subscribe()
                                        }
                                        Mono.just(grade)
                                    }
                                }
                                    .switchIfEmpty(Mono.error(TableReserveTicketNotFound()))
                                    .log()
                            }
                        }
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