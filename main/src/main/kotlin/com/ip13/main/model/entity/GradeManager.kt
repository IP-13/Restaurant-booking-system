package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class GradeManager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val managerId: Int = 0,
    val tableReserveTicketId: Int = 0,
    val userId: Int = 0,
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradeManager

        if (managerId != other.managerId) return false
        if (tableReserveTicketId != other.tableReserveTicketId) return false
        if (userId != other.userId) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = managerId
        result = 31 * result + tableReserveTicketId
        result = 31 * result + userId
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeManager(id=$id, managerId=$managerId, tableReserveTicketId=$tableReserveTicketId, " +
                "userId=$userId, grade=$grade, comment=$comment)"
    }
}