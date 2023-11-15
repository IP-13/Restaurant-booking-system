package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.Role
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

data class RoleDeleteRequestDto(
    @PositiveOrZero
    val userId: Int,
    @NotNull
    val role: Role,
)