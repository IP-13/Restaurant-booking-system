package com.ip13.main.model.entity

import com.ip13.main.model.enums.TableReserveStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class TableReserveTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val restaurantId: Int = 0,
    val userId: Int = 0,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
    val numOfGuests: Int = 0,
    val userComment: String? = null,
    val managerId: Int? = null,
    val managerComment: String? = null,
    @Enumerated(EnumType.STRING)
    val status: TableReserveStatus = TableReserveStatus.PROCESSING,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableReserveTicket

        if (restaurantId != other.restaurantId) return false
        if (userId != other.userId) return false
        if (creationDate != other.creationDate) return false
        if (fromDate != other.fromDate) return false
        if (tillDate != other.tillDate) return false
        if (numOfGuests != other.numOfGuests) return false
        if (userComment != other.userComment) return false
        if (managerId != other.managerId) return false
        if (managerComment != other.managerComment) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantId
        result = 31 * result + userId
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + tillDate.hashCode()
        result = 31 * result + numOfGuests
        result = 31 * result + (userComment?.hashCode() ?: 0)
        result = 31 * result + (managerId ?: 0)
        result = 31 * result + (managerComment?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "TableReserveTicket(id=$id, restaurantId=$restaurantId, userId=$userId, creationDate=$creationDate, " +
                "fromDate=$fromDate, tillDate=$tillDate, numOfGuests=$numOfGuests, userComment=$userComment, " +
                "managerId=$managerId, managerComment=$managerComment, status=$status)"
    }
}