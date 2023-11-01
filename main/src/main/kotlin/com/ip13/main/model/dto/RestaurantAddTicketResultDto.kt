package com.ip13.main.model.dto

import com.ip13.main.model.entity.enums.RestaurantAddResult

data class RestaurantAddTicketResultDto(
    val restaurantAddTicketId: Int,
    val adminId: Int,
    val result: RestaurantAddResult,
    val adminComment: String?,
)