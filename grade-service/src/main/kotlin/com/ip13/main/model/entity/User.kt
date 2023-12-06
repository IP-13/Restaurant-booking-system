package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class User(
    @Id
    val username: String,
    val numOfGrades: Int,
    val sumOfGrades: Int,
)