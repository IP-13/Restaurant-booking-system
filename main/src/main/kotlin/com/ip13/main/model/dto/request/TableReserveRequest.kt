package com.ip13.main.model.dto.request

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class TableReserveRequest(
    @PositiveOrZero
    val restaurantId: Int,
    @FutureOrPresent
    val fromDate: LocalDateTime,
    @Future
    val tillDate: LocalDateTime,
    @Positive
    val numOfGuests: Int,
    @Size(max = 25, message = "User comment length should be less than 25 symbols")
    val userComment: String?,
)