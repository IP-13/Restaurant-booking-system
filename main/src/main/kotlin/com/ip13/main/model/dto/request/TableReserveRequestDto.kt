package com.ip13.main.model.dto.request

import java.time.LocalDateTime

data class TableReserveRequestDto(
    val restaurantId: Int = 0,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
    val numOfGuests: Int = 0,
    val userComment: String? = null,
)