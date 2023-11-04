package com.ip13.main.service

import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.repository.BookingConstraintRepository
import org.springframework.stereotype.Service

@Service
class BookingConstraintService(
    private val bookingConstraintRepository: BookingConstraintRepository,
) {
    fun save(bookingConstraint: BookingConstraint): Int {
        return bookingConstraintRepository.save(bookingConstraint).id
    }
}