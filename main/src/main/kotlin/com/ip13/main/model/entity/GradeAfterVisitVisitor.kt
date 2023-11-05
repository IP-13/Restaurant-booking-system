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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradeAfterVisitVisitor

        if (userId != other.userId) return false
        if (tableReserveTicketResultId != other.tableReserveTicketResultId) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + tableReserveTicketResultId
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeAfterVisitVisitor(id=$id, userId=$userId, " +
                "tableReserveTicketResultId=$tableReserveTicketResultId, grade=$grade, comment=$comment)"
    }
}