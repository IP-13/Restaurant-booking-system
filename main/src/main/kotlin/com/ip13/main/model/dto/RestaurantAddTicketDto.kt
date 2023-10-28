package com.ip13.main.model.dto

data class RestaurantAddTicketDto(
    val name: String = "",
    val country: String = "",
    val city: String = "",
    val street: String = "",
    val building: Int = 0,
    val entrance: Int? = null,
    val floor: Int? = null,
    val description: String? = null,
    val userId: Int = 0,
    val authorComment: String? = null,
    val adminComment: String? = null,
)