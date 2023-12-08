package com.ip13.main.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("user_t")
data class User(
    @Id
    val id: Int = 0,
    val username: String,
    val numOfGrades: Int,
    val sumOfGrades: Int,
)