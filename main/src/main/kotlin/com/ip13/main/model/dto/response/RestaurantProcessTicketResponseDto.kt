package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.RestaurantAddStatus
import jakarta.validation.constraints.NotNull

data class RestaurantProcessTicketResponseDto(
    @NotNull
    val status: RestaurantAddStatus,
    val newRestaurantId: Int?,
)
