package com.ip13.main.model.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class GradeVisitorRequest(
    @field:Positive
    val tableReserveTicketId: Int,
    @field:Min(1)
    @field:Max(5)
    val grade: Int,
    @field:Size(max = 25, message = "Comment length should be less than 25 symbols")
    val comment: String?,
)