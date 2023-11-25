package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.security.model.dto.response.UserResponse
import java.time.LocalDateTime

data class TableReserveTicketResponse(
    val id: Int,
    val restaurant: RestaurantResponse,
    val user: UserResponse,
    val creationDate: LocalDateTime,
    val fromDate: LocalDateTime,
    val tillDate: LocalDateTime,
    val numOfGuests: Int,
    val userComment: String?,
    val manager: UserResponse?,
    val managerComment: String?,
    val status: TableReserveStatus,
)