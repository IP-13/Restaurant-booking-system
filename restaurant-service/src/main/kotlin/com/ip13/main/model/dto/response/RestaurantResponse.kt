package com.ip13.main.model.dto.response

data class RestaurantResponse(
    val id: Int,
    val restaurantAddTicketId: Int,
    val managerName: String,
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
    val description: String?,
)