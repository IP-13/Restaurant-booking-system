package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.Role

data class UserResponse(
    val id: Int,
    val username: String,
    val numOfGrades: Int,
    val sumOfGrades: Int,
    val roles: List<Role>,
)