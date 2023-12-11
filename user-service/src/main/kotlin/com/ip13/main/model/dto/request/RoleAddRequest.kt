package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.Role
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

data class RoleAddRequest(
    val username: String,
    @field:NotNull
    val role: Role,
)