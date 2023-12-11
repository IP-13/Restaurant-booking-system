package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class VisitorGrade(
    @Id
    val id: Int = 0,
    val managerName: String,
    val tableReserveTicketId: Int,
    val username: String,
    val grade: Int,
    val comment: String?,
)