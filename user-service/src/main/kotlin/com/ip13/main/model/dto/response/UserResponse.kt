package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.Role

data class UserResponse(
    val username: String,
    val roles: List<Role>,
)