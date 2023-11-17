package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class GradeManagerRequest(
    @Positive
    val tableReserveTicketId: Int,
    @Positive
    val grade: Int,
    @Size(max = 25, message = "Comment length should be less than 25 symbols")
    val comment: String?,
)