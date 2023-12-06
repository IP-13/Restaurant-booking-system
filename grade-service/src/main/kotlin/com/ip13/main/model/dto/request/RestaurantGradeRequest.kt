package com.ip13.main.model.dto.request

data class RestaurantGradeRequest(
    val tableReserveTicketId: Int,
    val grade: Int,
    val comment: String?,
)