package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val country: String = "",
    val city: String = "",
    val street: String = "",
    val building: Int = 0,
    val entrance: Int? = null,
    val floor: Int? = null,
) {
    override fun equals(other: Any?): Boolean {
        return other === this ||
                (other is Address &&
                        other.country == this.country &&
                        other.city == this.city &&
                        other.street == this.street &&
                        other.building == this.building &&
                        other.entrance == this.entrance &&
                        other.floor == this.floor
                        )
    }

    override fun hashCode(): Int {
        var result = country.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + building
        result = 31 * result + (entrance ?: 0)
        result = 31 * result + (floor ?: 0)
        return result
    }
}