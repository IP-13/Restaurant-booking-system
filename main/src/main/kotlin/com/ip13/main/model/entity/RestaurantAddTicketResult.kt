package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.RestaurantAddStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val restaurantAddTicketId: Int = 0,
    val adminId: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val result: RestaurantAddStatus = RestaurantAddStatus.PROCESSING,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val adminComment: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantAddTicketResult

        if (restaurantAddTicketId != other.restaurantAddTicketId) return false
        if (adminId != other.adminId) return false
        if (result != other.result) return false
        if (creationDate != other.creationDate) return false
        if (adminComment != other.adminComment) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = restaurantAddTicketId
        result1 = 31 * result1 + adminId
        result1 = 31 * result1 + result.hashCode()
        result1 = 31 * result1 + creationDate.hashCode()
        result1 = 31 * result1 + (adminComment?.hashCode() ?: 0)
        return result1
    }

    override fun toString(): String {
        return "RestaurantAddTicketResult(id=$id, restaurantAddTicketId=$restaurantAddTicketId, adminId=$adminId, " +
                "result=$result, creationDate=$creationDate, adminComment=$adminComment)"
    }
}