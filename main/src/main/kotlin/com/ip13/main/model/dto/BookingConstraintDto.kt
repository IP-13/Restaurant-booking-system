package com.ip13.main.model.dto

import java.time.LocalDateTime

data class BookingConstraintDto(
    val restaurantId: Int = 0,
    val managerId: Int = 0,
    val reason: String? = null,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val expirationDate: LocalDateTime = LocalDateTime.now(),
)