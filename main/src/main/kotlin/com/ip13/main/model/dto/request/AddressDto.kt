package com.ip13.main.model.dto.request

import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class AddressDto(
    @Size(min = 3, max = 30, message = "Country name should be from 3 to 30 symbols")
    val country: String = "",
    @Size(min = 3, max = 30, message = "City name should be from 3 to 30 symbols")
    val city: String = "",
    @Size(min = 2, max = 50, message = "Street name should be from 2 to 50 symbols")
    val street: String = "",
    @PositiveOrZero
    val building: Int = 0,
    val entrance: Int? = null,
    val floor: Int? = null,
)