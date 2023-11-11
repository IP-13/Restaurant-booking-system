package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus

data class ReservationProcessResponseDto(
    val id: Int,
    val status: TableReserveStatus,
)