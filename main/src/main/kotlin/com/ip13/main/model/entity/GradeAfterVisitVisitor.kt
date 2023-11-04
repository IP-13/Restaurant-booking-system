package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class GradeAfterVisitVisitor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val userId: Int = 0,
    val tableReserveTicketResultId: Int = 0,
    val grade: Int = 0,
    val comment: String? = null,
)