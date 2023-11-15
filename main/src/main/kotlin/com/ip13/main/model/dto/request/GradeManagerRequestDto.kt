package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class GradeManagerRequestDto(
    @Positive
    val tableReserveTicketId: Int,
    @Positive
    val grade: Int,
    @Size(min = 3, max = 25, message = "Comment should be from 3 to 25 symbols")
    val comment: String?,
)