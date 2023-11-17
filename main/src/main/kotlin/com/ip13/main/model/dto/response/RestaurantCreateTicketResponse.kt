package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.RestaurantAddStatus

data class RestaurantCreateTicketResponse(
    val status: RestaurantAddStatus,
)