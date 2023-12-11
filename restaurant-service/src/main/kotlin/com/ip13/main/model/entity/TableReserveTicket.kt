package com.ip13.main.model.entity

import com.ip13.main.model.enums.TableReserveStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class TableReserveTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    val username: String = "",
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
    val numOfGuests: Int = 0,
    val userComment: String? = null,
    val managerName: String? = null,
    val managerComment: String? = null,
    @Enumerated(EnumType.STRING)
    val status: TableReserveStatus = TableReserveStatus.PROCESSING,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableReserveTicket

        if (restaurant != other.restaurant) return false
        if (username != other.username) return false
        if (creationDate != other.creationDate) return false
        if (fromDate != other.fromDate) return false
        if (tillDate != other.tillDate) return false
        if (numOfGuests != other.numOfGuests) return false
        if (userComment != other.userComment) return false
        if (managerName != other.managerName) return false
        if (managerComment != other.managerComment) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurant.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + tillDate.hashCode()
        result = 31 * result + numOfGuests
        result = 31 * result + (userComment?.hashCode() ?: 0)
        result = 31 * result + (managerName?.hashCode() ?: 0)
        result = 31 * result + (managerComment?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "TableReserveTicket(id=$id, restaurant=$restaurant, username='$username', creationDate=$creationDate," +
                " fromDate=$fromDate, tillDate=$tillDate, numOfGuests=$numOfGuests, userComment=$userComment," +
                " managerName=$managerName, managerComment=$managerComment, status=$status)"
    }
}