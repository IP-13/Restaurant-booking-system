package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.RestaurantAddStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class RestaurantProcessTicketRequest(
    @field:Positive
    val restaurantAddTicketId: Int,
    @field:NotNull
    val status: RestaurantAddStatus,
    @field:Size(max = 25, message = "Administrator comment length should be less than 25 symbols")
    val adminComment: String?,
)