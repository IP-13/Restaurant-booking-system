package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.RestaurantAddStatus

data class RestaurantProcessTicketResponse(
    val status: RestaurantAddStatus,
    val newRestaurantId: Int?,
)
