package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class User(
    @Id
    val id: Int,
    val numOfGrades: Int,
    val sumOfGrades: Int,
)