package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val addressId: Int = 0,
    val restaurantAddTicketId: Int = 0,
    val description: String? = null,
    val numOfGrades: Int = 0,
    val sumOfGrades: Int = 0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (name != other.name) return false
        if (addressId != other.addressId) return false
        if (restaurantAddTicketId != other.restaurantAddTicketId) return false
        if (description != other.description) return false
        if (numOfGrades != other.numOfGrades) return false
        if (sumOfGrades != other.sumOfGrades) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + addressId
        result = 31 * result + restaurantAddTicketId
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + numOfGrades
        result = 31 * result + sumOfGrades
        return result
    }

    override fun toString(): String {
        return "Restaurant(id=$id, name='$name', addressId=$addressId, restaurantAddTicketId=$restaurantAddTicketId, " +
                "description=$description, numOfGrades=$numOfGrades, sumOfGrades=$sumOfGrades)"
    }
}