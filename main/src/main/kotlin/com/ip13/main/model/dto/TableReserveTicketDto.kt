package com.ip13.main.model.dto

data class TableReserveTicketDto(
    val restaurantId: Int,
    val userId: Int,
    val userComment: String?,
)