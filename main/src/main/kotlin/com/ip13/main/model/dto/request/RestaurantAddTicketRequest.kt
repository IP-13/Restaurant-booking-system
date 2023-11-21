package com.ip13.main.model.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class RestaurantAddTicketRequest(
    @field:NotBlank(message = "Restaurant name can't be blank")
    val name: String,
    @field:Size(min = 3, max = 30, message = "Country name should be from 3 to 30 symbols")
    val country: String,
    @field:Size(min = 3, max = 30, message = "City name should be from 3 to 30 symbols")
    val city: String,
    @field:Size(min = 2, max = 50, message = "Street name should be from 2 to 50 symbols")
    val street: String,
    @field:PositiveOrZero
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
    @field:Size(max = 50, message = "Restaurant description should be less than 50 symbols")
    val description: String?,
)