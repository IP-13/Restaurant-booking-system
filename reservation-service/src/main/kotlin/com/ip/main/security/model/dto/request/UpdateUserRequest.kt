package com.ip13.main.security.model.dto.request

import com.ip13.main.security.model.enums.Role

data class UpdateUserRequest(
    val id: Int,
    val numOfGrades: Int,
    val sumOfGrades: Int,
    val roles: List<Role>,
)