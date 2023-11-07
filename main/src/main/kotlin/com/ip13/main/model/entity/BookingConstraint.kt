package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class BookingConstraint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    @ManyToOne
    @JoinColumn(name = "manager_id")
    val manager: User = User(),
    val reason: String? = null,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookingConstraint

        if (restaurant != other.restaurant) return false
        if (manager != other.manager) return false
        if (reason != other.reason) return false
        if (fromDate != other.fromDate) return false
        if (tillDate != other.tillDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurant.hashCode()
        result = 31 * result + manager.hashCode()
        result = 31 * result + (reason?.hashCode() ?: 0)
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + tillDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "BookingConstraint(id=$id, restaurant=$restaurant, manager=$manager, reason=$reason, " +
                "fromDate=$fromDate, tillDate=$tillDate)"
    }
}