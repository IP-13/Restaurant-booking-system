package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.RestaurantAddStatus

data class RestaurantProcessTicketRequestDto(
    val restaurantAddTicketId: Int,
    val status: RestaurantAddStatus,
    val adminComment: String?,
)