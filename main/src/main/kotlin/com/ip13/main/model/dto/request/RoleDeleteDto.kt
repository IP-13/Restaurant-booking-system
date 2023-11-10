package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.Role

data class RoleDeleteDto(
    val userId: Int,
    val role: Role,
)