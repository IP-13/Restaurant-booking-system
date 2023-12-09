package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class RestaurantGrade(
    @Id
    val id: Int = 0,
    val username: Int,
    val tableReserveTicketId: Int,
    val restaurantId: Int,
    val grade: Int,
    val comment: String?,
)