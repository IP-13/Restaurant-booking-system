package com.ip13.main.model.dto

data class RestaurantAddTicketDto(
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
    val description: String?,
    val userId: Int,
    val authorComment: String?,
    val adminComment: String?,
)