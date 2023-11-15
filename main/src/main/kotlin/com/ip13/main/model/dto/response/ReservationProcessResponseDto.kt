package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

data class ReservationProcessResponseDto(
    @PositiveOrZero
    val id: Int,
    @NotNull
    val status: TableReserveStatus,
)