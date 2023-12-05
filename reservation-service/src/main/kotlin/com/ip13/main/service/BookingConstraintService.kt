package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.feign.restaurantClient.RestaurantClient
import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.response.AddBookingConstraintResponse
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.toBookingConstraint
import com.ip13.main.repository.BookingConstraintRepository
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class BookingConstraintService(
    private val bookingConstraintRepository: BookingConstraintRepository,
    private val restaurantClient: RestaurantClient,
    private val userClient: UserClient,
) {
    private val log = getLogger(javaClass)

    fun save(bookingConstraint: BookingConstraint): BookingConstraint {
        return bookingConstraintRepository.save(bookingConstraint)
    }

    fun addBookingConstraint(
        request: AddBookingConstraintRequest,
        username: String,
        authHeader: String,
    ): AddBookingConstraintResponse {
        val restaurant = restaurantClient.getRestaurantById(authHeader = authHeader, id = request.restaurantId)
            ?: throw RestaurantNotFoundException()

        log.debug("Restaurant found\n{}", restaurant.toString())

        val manager = userClient.getUserByUsername(authHeader = authHeader, username = username)

        log.debug("user extracted from token\n{}", manager.toString())

        if (restaurant.managerId != manager.id) {
            throw CommonException(
                "User ${manager.id} is not manager of restaurant ${restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val bookingConstraint = request.toBookingConstraint(manager.id)

        return AddBookingConstraintResponse(
            id = save(bookingConstraint).id
        )
    }
}