package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class TableReserveRequest(
    @Positive
    val restaurantId: Int,
    @FutureOrPresent
    val fromDate: LocalDateTime = LocalDateTime.now(),
    @Future
    val tillDate: LocalDateTime = LocalDateTime.now(),
    @Positive
    val numOfGuests: Int,
    @Size(max = 25, message = "User comment length should be less than 25 symbols")
    val userComment: String? = null,
)