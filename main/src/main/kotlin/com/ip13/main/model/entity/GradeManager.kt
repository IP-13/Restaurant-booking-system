package com.ip13.main.model.entity

import com.ip13.main.security.model.entity.User
import jakarta.persistence.*


@Entity
class GradeManager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "manager_id")
    val manager: User = User(),
    @ManyToOne
    @JoinColumn(name = "table_reserve_ticket_id")
    val tableReserveTicket: TableReserveTicket = TableReserveTicket(),
    // Пользователь, которому менеджер ставит оценку
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    val grade: Int = 0,
    val comment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradeManager

        if (manager != other.manager) return false
        if (tableReserveTicket != other.tableReserveTicket) return false
        if (user != other.user) return false
        if (grade != other.grade) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manager.hashCode()
        result = 31 * result + tableReserveTicket.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + grade
        result = 31 * result + (comment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GradeManager(id=$id, manager=$manager, tableReserveTicket=$tableReserveTicket, user=$user, " +
                "grade=$grade, comment=$comment)"
    }
}