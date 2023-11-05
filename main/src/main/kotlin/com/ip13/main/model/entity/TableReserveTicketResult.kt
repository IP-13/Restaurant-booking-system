package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.ReserveTableStatus
import jakarta.persistence.*

@Entity
class TableReserveTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val managerId: Int = 0,
    val managerComment: String? = null,
    @Enumerated(value = EnumType.STRING)
    val status: ReserveTableStatus = ReserveTableStatus.PROCESSING,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableReserveTicketResult

        if (managerId != other.managerId) return false
        if (managerComment != other.managerComment) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = managerId
        result = 31 * result + (managerComment?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "TableReserveTicketResult(id=$id, managerId=$managerId, managerComment=$managerComment, status=$status)"
    }
}