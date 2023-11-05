package com.ip13.main.model.dto

import java.time.LocalDateTime

data class TableReserveTicketDto(
    val restaurantId: Int = 0,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
    val numOfGuests: Int = 0,
    val userComment: String? = null,
)