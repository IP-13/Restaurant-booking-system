package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class Restaurant(
    @Id
    val id: Int = 0,
    val numOfGrades: Int,
    val sumOfGrades: Int,
)