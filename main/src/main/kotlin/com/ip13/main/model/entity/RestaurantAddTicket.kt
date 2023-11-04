package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val country: String = "",
    val city: String = "",
    val street: String = "",
    val building: Int = 0,
    val entrance: Int? = null,
    val floor: Int? = null,
    val description: String? = null,
    val userId: Int = 0,
    val createDate: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        return other === this ||
                (other is RestaurantAddTicket &&
                        other.name == this.name &&
                        other.country == this.country &&
                        other.city == this.city &&
                        other.street == this.street &&
                        other.building == this.building &&
                        other.entrance == this.entrance &&
                        other.floor == this.floor &&
                        other.description == this.description &&
                        other.userId == this.userId &&
                        other.createDate == this.createDate
                        )
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + building
        result = 31 * result + (entrance ?: 0)
        result = 31 * result + (floor ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + userId
        result = 31 * result + createDate.hashCode()
        return result
    }
}