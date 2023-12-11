package com.ip13.main.handler

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFound
import com.ip13.main.model.dto.request.RestaurantGradeRequest
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.entity.User
import com.ip13.main.repository.RestaurantGradeRepository
import com.ip13.main.util.getLogger
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.server.ResponseStatusException

@Component
class RestaurantGradeHandler(
    private val restaurantGradeRepository: RestaurantGradeRepository,
    private val restaurantCoHandler: RestaurantCoHandler,
    private val userCoHandler: UserCoHandler,
    private val restaurantServiceWebClient: RestaurantServiceWebClient,
) {
    private val log = getLogger(javaClass)

    suspend fun gradeRestaurant(request: ServerRequest): ServerResponse {
        log.debug("in grade restaurant method")

        val requestBody = request.awaitBody(RestaurantGradeRequest::class)
        val tableReserveTicket = restaurantServiceWebClient.suspendGetTableReserveTicketOrNull(
            requestBody.tableReserveTicketId,
            request.headers().header(HttpHeaders.AUTHORIZATION).first()
        )

        if (tableReserveTicket == null) {
            throw TableReserveTicketNotFound()
        }

        if (restaurantGradeRepository.existsByTableReserveTicketId(tableReserveTicket.id)) {
            throw CommonException(
                "ticket with id ${tableReserveTicket.id} already graded",
                HttpStatus.BAD_REQUEST
            )
        }

        log.debug("after checking tableReserveTicket")

        if (tableReserveTicket.username != request.principal().awaitSingle().name) {
            throw ResponseStatusException(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Attempt to grade ticket which doesn't belong to you"
            )
        }

        if (restaurantCoHandler.findByRestaurantIdOrNull(tableReserveTicket.restaurantId) == null) {
            restaurantCoHandler.save(
                Restaurant(
                    restaurantId = tableReserveTicket.restaurantId,
                    numOfGrades = 1,
                    sumOfGrades = requestBody.grade,
                )
            )
        }

        if (userCoHandler.findByUsernameOrNull(tableReserveTicket.username) == null) {
            userCoHandler.save(
                User(
                    username = tableReserveTicket.username,
                    numOfGrades = 0,
                    sumOfGrades = 0,
                )
            )
        }

        restaurantGradeRepository.save(
            RestaurantGrade(
                username = tableReserveTicket.username,
                tableReserveTicketId = requestBody.tableReserveTicketId,
                restaurantId = tableReserveTicket.restaurantId,
                grade = requestBody.grade,
                comment = requestBody.comment,
            )
        )

        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .bodyValueAndAwait(
                restaurantCoHandler.getGrade(tableReserveTicket.restaurantId)
            )
    }

    suspend fun getGrade(request: ServerRequest): ServerResponse {
        log.debug("in get grade restaurant method")
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(
                restaurantCoHandler.getGrade(request.pathVariable("id").toInt())
            )
    }
}