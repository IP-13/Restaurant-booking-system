package com.ip13.main.model.dto

import com.ip13.main.model.enums.Role

data class RoleAddDto(
    val userId: Int,
    val role: Role,
)