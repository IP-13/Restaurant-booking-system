package com.ip13.main.model.dto.response

import com.ip13.main.model.entity.TableReserveTicket

data class ShowReservationsResponseDto(
    val reservations: List<TableReserveTicket>
)