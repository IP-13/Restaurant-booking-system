package com.ip13.main.model.dto.response

import jakarta.validation.constraints.PositiveOrZero

data class GradeVisitorResponseDto(
    @PositiveOrZero
    val newGrade: Float,
)