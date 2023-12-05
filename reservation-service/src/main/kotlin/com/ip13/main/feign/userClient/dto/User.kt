package com.ip13.main.feign.userClient.dto

import com.ip13.main.model.enums.Role

data class User(
    val id: Int,
    val username: String,
    val numOfGrades: Int,
    val sumOfGrades: Int,
    val roles: List<Role> = listOf(),
)