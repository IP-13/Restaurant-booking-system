package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class TableReserveRequest(
    @PositiveOrZero
    val restaurantId: Int = 0,
    @FutureOrPresent
    val fromDate: LocalDateTime = LocalDateTime.now(),
    @Future
    val tillDate: LocalDateTime = LocalDateTime.now(),
    @PositiveOrZero
    val numOfGuests: Int = 0,
    @Size(max = 25, message = "User comment length should be less than 25 symbols")
    val userComment: String? = null,
)