package com.ip13.main.model.entity

import jakarta.persistence.*


@Entity
class VisitorGrade(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val managerId: Int = 0,
    @ManyToOne
    @JoinColumn(name = "table_reserve_ticket_id")
    val tableReserveTicket: TableReserveTicket = TableReserveTicket(),
    // Пользователь, которому менеджер ставит оценку
    val userId: Int = 0,
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisitorGrade

        if (managerId != other.managerId) return false
        if (tableReserveTicket != other.tableReserveTicket) return false
        if (userId != other.userId) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = managerId.hashCode()
        result = 31 * result + tableReserveTicket.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeManager(id=$id, manager=$managerId, tableReserveTicket=$tableReserveTicket, user=$userId, " +
                "grade=$grade, comment=$comment)"
    }
}