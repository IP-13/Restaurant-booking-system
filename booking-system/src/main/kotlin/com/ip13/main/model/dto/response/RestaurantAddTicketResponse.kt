package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.security.model.dto.response.UserResponse
import java.time.LocalDateTime

data class RestaurantAddTicketResponse(
    val id: Int,
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
    val description: String?,
    val user: UserResponse,
    val creationDate: LocalDateTime?,
    val status: RestaurantAddStatus = RestaurantAddStatus.PROCESSING,
    val admin: UserResponse?,
    val processingDate: LocalDateTime?,
    val adminComment: String?,
)