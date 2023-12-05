package com.ip13.main.model.entity

import org.springframework.data.annotation.Id

data class VisitorGrade(
    @Id
    val id: Int,
    val managerId: Int,
    val tableReserveTicketId: Int,
    val userId: Int,
    val grade: Int,
    val comment: String?,
)