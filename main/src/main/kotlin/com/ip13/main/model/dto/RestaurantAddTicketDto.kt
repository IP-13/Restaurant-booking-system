package com.ip13.main.model.dto

data class RestaurantAddTicketDto(
    val name: String,
    val addressDto: AddressDto,
    val description: String?,
    val userId: Int,
)