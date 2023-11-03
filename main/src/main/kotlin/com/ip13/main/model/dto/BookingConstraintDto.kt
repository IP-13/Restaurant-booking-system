package com.ip13.main.model.dto

import java.time.LocalDateTime

data class BookingConstraintDto(
    val restaurantId: Int,
    val managerId: Int,
    val reason: String?,
    val creationDate: LocalDateTime,
    val expirationDate: LocalDateTime,
)