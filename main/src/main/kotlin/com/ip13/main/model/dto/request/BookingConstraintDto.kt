package com.ip13.main.model.dto.request

import java.time.LocalDateTime

data class BookingConstraintDto(
    val restaurantId: Int = 0,
    val reason: String? = null,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
)