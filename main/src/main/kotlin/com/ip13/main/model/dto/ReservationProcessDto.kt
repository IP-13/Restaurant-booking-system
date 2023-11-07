package com.ip13.main.model.dto

import com.ip13.main.model.enums.TableReserveStatus

data class ReservationProcessDto(
    val tableReserveTicketId: Int,
    val managerComment: String?,
    val status: TableReserveStatus,
)