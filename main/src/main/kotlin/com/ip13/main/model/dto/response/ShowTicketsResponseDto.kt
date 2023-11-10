package com.ip13.main.model.dto.response

import com.ip13.main.model.entity.RestaurantAddTicket

data class ShowTicketsResponseDto(
    val tickets: List<RestaurantAddTicket>,
)