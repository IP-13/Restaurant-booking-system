package com.ip13.main.model

import com.ip13.main.model.dto.response.UserResponse
import com.ip13.main.model.entity.User

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        username = this.username,
        roles = this.roles
    )
}