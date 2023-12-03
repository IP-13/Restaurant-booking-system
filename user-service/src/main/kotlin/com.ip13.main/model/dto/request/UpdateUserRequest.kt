package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.Role

data class UpdateUserRequest(
    val id: Int,
    val numOfGrades: Int,
    val sumOfGrades: Int,
    val roles: List<Role>,
)