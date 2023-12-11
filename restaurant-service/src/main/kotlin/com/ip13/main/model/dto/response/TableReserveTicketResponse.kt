package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus
import java.time.LocalDateTime

data class TableReserveTicketResponse(
    val id: Int,
    val restaurantId: Int,
    val username: String,
    val creationDate: LocalDateTime,
    val fromDate: LocalDateTime,
    val tillDate: LocalDateTime,
    val numOfGuests: Int,
    val userComment: String?,
    val managerName: String?,
    val managerComment: String?,
    val status: TableReserveStatus,
)
