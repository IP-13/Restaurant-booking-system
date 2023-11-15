package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class AddBookingConstraintRequestDto(
    @PositiveOrZero
    val restaurantId: Int = 0,
    @Size(min = 5, max = 15, message = "Reason message should be from 5 to 15 symbols")
    val reason: String? = null,
    @FutureOrPresent
    val fromDate: LocalDateTime = LocalDateTime.now(),
    @Future
    val tillDate: LocalDateTime = LocalDateTime.now(),
)