package com.ip13.main.model.entity

import com.ip13.main.security.model.entity.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
class GradeVisitor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    @ManyToOne
    @JoinColumn(name = "table_reserve_ticket_id")
    val tableReserveTicket: TableReserveTicket = TableReserveTicket(),
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradeVisitor

        if (user != other.user) return false
        if (tableReserveTicket != other.tableReserveTicket) return false
        if (restaurant != other.restaurant) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + tableReserveTicket.hashCode()
        result = 31 * result + restaurant.hashCode()
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeVisitor(id=$id, user=$user, tableReserveTicket=$tableReserveTicket, " +
                "restaurant=$restaurant, grade=$grade, comment=$comment)"
    }
}