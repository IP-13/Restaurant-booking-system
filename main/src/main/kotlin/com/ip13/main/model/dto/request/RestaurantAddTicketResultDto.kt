package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.RestaurantAddStatus

data class RestaurantAddTicketResultDto(
    val restaurantAddTicketId: Int = 0,
    val result: RestaurantAddStatus = RestaurantAddStatus.PROCESSING,
    val adminComment: String? = null,
)