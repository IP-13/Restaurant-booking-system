package com.ip13.main.model.dto.request

data class RestaurantAddTicketDto(
    val name: String = "",
    val addressDto: AddressDto = AddressDto(),
    val description: String? = null,
)