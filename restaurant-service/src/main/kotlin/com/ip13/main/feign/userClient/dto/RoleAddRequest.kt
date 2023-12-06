package com.ip13.main.feign.userClient.dto

import com.ip13.main.model.enums.Role

data class RoleAddRequest(
    val username: String,
    val role: Role,
)