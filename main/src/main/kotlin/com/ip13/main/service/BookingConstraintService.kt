package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.BookingConstraintRequestDto
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.toBookingConstraint
import com.ip13.main.repository.BookingConstraintRepository
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BookingConstraintService(
    private val bookingConstraintRepository: BookingConstraintRepository,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
) {
    private val log = getLogger(javaClass)

    fun save(bookingConstraint: BookingConstraint): BookingConstraint {
        return bookingConstraintRepository.save(bookingConstraint)
    }

    fun isOpen(fromDate: LocalDateTime, tillDate: LocalDateTime, restaurantId: Int): Int {
        return bookingConstraintRepository.isOpen(fromDate, tillDate, restaurantId)
    }

    fun addBookingConstraint(authHeader: String, dto: BookingConstraintRequestDto) {
        val restaurant = restaurantService.findByIdOrThrow(dto.restaurantId)

        log.debug("Restaurant found\n{}", restaurant.toString())

        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        if (restaurant.manager.id != user.id) {
            throw CommonException(
                "User ${user.id} is not manager of restaurant ${restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val bookingConstraint = dto.toBookingConstraint(restaurant, user)

        save(bookingConstraint)
    }
}