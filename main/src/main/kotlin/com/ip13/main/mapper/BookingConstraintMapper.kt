package com.ip13.main.mapper

import com.ip13.main.model.dto.BookingConstraintDto
import com.ip13.main.model.entity.BookingConstraint

object BookingConstraintMapper {
    fun fromBookingConstraintDto(dto: BookingConstraintDto, managerId: Int): BookingConstraint {
        return BookingConstraint(
            restaurantId = dto.restaurantId,
            managerId = managerId,
            reason = dto.reason,
            fromDate = dto.fromDate,
            tillDate = dto.tillDate,
        )
    }
}