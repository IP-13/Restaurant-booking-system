package com.ip13.main.model.dto.response

import jakarta.validation.constraints.PositiveOrZero

data class AddBookingConstraintResponseDto(
    @PositiveOrZero
    val id: Int,
)