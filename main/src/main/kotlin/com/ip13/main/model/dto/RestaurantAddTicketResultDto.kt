package com.ip13.main.model.dto

import com.ip13.main.model.entity.enums.RestaurantAddStatus

data class RestaurantAddTicketResultDto(
    val restaurantAddTicketId: Int = 0,
    val adminId: Int = 0,
    val result: RestaurantAddStatus = RestaurantAddStatus.PROCESSING,
    val adminComment: String? = null,
)