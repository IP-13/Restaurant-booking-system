package com.ip13.main.model.dto.request

data class RestaurantAddTicketRequestDto(
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
    val description: String? = null,
)