package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val addressId: Int = 0,
    val description: String? = null,
    val userId: Int = 0,
    val creationDate: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantAddTicket

        if (name != other.name) return false
        if (addressId != other.addressId) return false
        if (description != other.description) return false
        if (userId != other.userId) return false
        if (creationDate != other.creationDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + addressId
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + userId
        result = 31 * result + creationDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "RestaurantAddTicket(id=$id, name='$name', addressId=$addressId, description=$description, " +
                "userId=$userId, creationDate=$creationDate)"
    }
}