package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Manager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val userId: Int = 0,
    val restaurantId: Int = 0,
    val isActive: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Manager

        if (userId != other.userId) return false
        if (restaurantId != other.restaurantId) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + restaurantId
        result = 31 * result + isActive.hashCode()
        return result
    }

    override fun toString(): String {
        return "Manager(id=$id, userId=$userId, restaurantId=$restaurantId, isActive=$isActive)"
    }
}