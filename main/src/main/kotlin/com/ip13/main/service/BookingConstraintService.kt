package com.ip13.main.service

import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.repository.BookingConstraintRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BookingConstraintService(
    private val bookingConstraintRepository: BookingConstraintRepository,
) {
    fun save(bookingConstraint: BookingConstraint): Int {
        return bookingConstraintRepository.save(bookingConstraint).id
    }

    fun isOpen(fromDate: LocalDateTime, tillDate: LocalDateTime, restaurantId: Int): Int {
        return bookingConstraintRepository.isOpen(fromDate, tillDate, restaurantId)
    }
}