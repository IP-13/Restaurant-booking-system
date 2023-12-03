package com.ip13.main.security.model

import com.ip13.main.security.model.dto.response.UserResponse
import com.ip13.main.security.model.entity.User

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        username = this.username,
        numOfGrades = this.numOfGrades,
        sumOfGrades = this.sumOfGrades,
        roles = this.roles,
    )
}