package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.RestaurantAddStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class RestaurantProcessTicketRequestDto(
    @Positive
    val restaurantAddTicketId: Int,
    @NotNull
    val status: RestaurantAddStatus,
    @Size(max = 25, message = "Administrator comment length should be less than 25 symbols")
    val adminComment: String?,
)