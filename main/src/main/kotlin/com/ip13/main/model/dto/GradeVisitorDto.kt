package com.ip13.main.model.dto

data class GradeVisitorDto(
    val tableReserveTicketId: Int,
    val restaurantId: Int,
    val grade: Int,
    val comment: String?,
)