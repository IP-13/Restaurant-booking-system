package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.Role
import jakarta.validation.constraints.NotNull

data class RoleAddRequest(
    val username: String,
    @field:NotNull
    val role: Role,
)