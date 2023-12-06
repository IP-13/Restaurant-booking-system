package com.ip13.main.model.dto.request

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class TableReserveRequest(
    @field:PositiveOrZero
    val restaurantId: Int,
    @field:FutureOrPresent
    val fromDate: LocalDateTime,
    @field:Future
    val tillDate: LocalDateTime,
    @field:Positive
    val numOfGuests: Int,
    @field:Size(max = 25, message = "User comment length should be less than 25 symbols")
    val userComment: String?,
)