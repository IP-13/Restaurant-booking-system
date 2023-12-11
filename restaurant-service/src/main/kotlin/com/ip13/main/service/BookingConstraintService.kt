package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
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
    private val restaurantService: RestaurantService,
) {
    private val log = getLogger(javaClass)

    fun save(bookingConstraint: BookingConstraint): BookingConstraint {
        return bookingConstraintRepository.save(bookingConstraint)
    }

    fun addBookingConstraint(
        request: AddBookingConstraintRequest,
        managerName: String,
    ): AddBookingConstraintResponse {
        val restaurant = restaurantService.findByIdOrThrow(request.restaurantId)

        log.debug("Restaurant found\n{}", restaurant.toString())

        if (restaurant.managerName != managerName) {
            throw CommonException(
                "User $managerName is not manager of restaurant ${restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val bookingConstraint = request.toBookingConstraint(restaurant, managerName)

        return AddBookingConstraintResponse(
            id = save(bookingConstraint).id
        )
    }
}