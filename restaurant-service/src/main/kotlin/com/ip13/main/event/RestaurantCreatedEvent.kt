package com.ip13.main.event

data class RestaurantCreatedEvent(
    val restaurantId: Int,
    val managerName: String,
)
