package com.ip13.main.model.dto

data class RestaurantAddTicketDto(
    val name: String = "",
    val addressDto: AddressDto = AddressDto(),
    val description: String? = null,
    val userId: Int = 0,
)