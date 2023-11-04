package com.ip13.main.model.dto

import com.ip13.main.model.entity.enums.RestaurantAddStatus

data class RestaurantAddTicketResultDto(
    val restaurantAddTicketId: Int,
    val adminId: Int,
    val result: RestaurantAddStatus,
    val adminComment: String?,
)