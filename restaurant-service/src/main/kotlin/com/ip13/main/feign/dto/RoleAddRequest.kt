package com.ip13.main.feign.dto

import com.ip13.main.model.enums.Role

data class RoleAddRequest(
    val userId: Int,
    val role: Role,
)