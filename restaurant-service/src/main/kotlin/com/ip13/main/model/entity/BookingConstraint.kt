package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BookingConstraint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    val managerId: Int = 0,
    val reason: String? = null,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookingConstraint

        if (restaurant != other.restaurant) return false
        if (managerId != other.managerId) return false
        if (reason != other.reason) return false
        if (fromDate != other.fromDate) return false
        if (tillDate != other.tillDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurant.hashCode()
        result = 31 * result + managerId
        result = 31 * result + (reason?.hashCode() ?: 0)
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + tillDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "BookingConstraint(id=$id, restaurant=$restaurant, managerId=$managerId, reason=$reason, " +
                "fromDate=$fromDate, tillDate=$tillDate)"
    }
}