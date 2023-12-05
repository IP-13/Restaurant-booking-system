package com.ip13.main.model.entity

import jakarta.persistence.*

@Entity
class RestaurantGrade(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val userId: Int = 0,
    @ManyToOne
    @JoinColumn(name = "table_reserve_ticket_id")
    val tableReserveTicket: TableReserveTicket = TableReserveTicket(),
    val restaurantId: Int = 0,
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantGrade

        if (userId != other.userId) return false
        if (tableReserveTicket != other.tableReserveTicket) return false
        if (restaurantId != other.restaurantId) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + tableReserveTicket.hashCode()
        result = 31 * result + restaurantId.hashCode()
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeVisitor(id=$id, user=$userId, tableReserveTicket=$tableReserveTicket, " +
                "restaurant=$restaurantId, grade=$grade, comment=$comment)"
    }
}