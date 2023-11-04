package com.ip13.main.model.dto

data class AddressDto(
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
)