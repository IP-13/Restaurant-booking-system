package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class GradeVisitor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val userId: Int = 0,
    val tableReserveTicketId: Int = 0,
    val restaurantId: Int = 0,
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradeVisitor

        if (userId != other.userId) return false
        if (tableReserveTicketId != other.tableReserveTicketId) return false
        if (restaurantId != other.restaurantId) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + tableReserveTicketId
        result = 31 * result + restaurantId
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeVisitor(id=$id, userId=$userId, tableReserveTicketId=$tableReserveTicketId, " +
                "restaurantId=$restaurantId, grade=$grade, comment=$comment)"
    }


}