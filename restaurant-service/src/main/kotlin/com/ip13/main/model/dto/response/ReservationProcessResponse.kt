package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus

data class ReservationProcessResponse(
    val id: Int,
    val status: TableReserveStatus,
)