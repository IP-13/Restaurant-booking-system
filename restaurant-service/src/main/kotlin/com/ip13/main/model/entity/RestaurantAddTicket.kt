package com.ip13.main.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ip13.main.model.enums.RestaurantAddStatus
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
    val username: String = "",
    val creationDate: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING)
    val status: RestaurantAddStatus = RestaurantAddStatus.PROCESSING,
    val adminName: String? = null,
    val processingDate: LocalDateTime? = null,
    val adminComment: String? = null,
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "restaurantAddTicket")
    val restaurant: Restaurant? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RestaurantAddTicket

        if (name != other.name) return false
        if (country != other.country) return false
        if (city != other.city) return false
        if (street != other.street) return false
        if (building != other.building) return false
        if (entrance != other.entrance) return false
        if (floor != other.floor) return false
        if (description != other.description) return false
        if (username != other.username) return false
        if (creationDate != other.creationDate) return false
        if (status != other.status) return false
        if (adminName != other.adminName) return false
        if (processingDate != other.processingDate) return false
        if (adminComment != other.adminComment) return false

        return true
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
        result = 31 * result + username.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (adminName?.hashCode() ?: 0)
        result = 31 * result + (processingDate?.hashCode() ?: 0)
        result = 31 * result + (adminComment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RestaurantAddTicket(id=$id, name='$name', country='$country', city='$city', street='$street', " +
                "building=$building, entrance=$entrance, floor=$floor, description=$description, userId=$username, " +
                "creationDate=$creationDate, status=$status, adminId=$adminName, processingDate=$processingDate," +
                " adminComment=$adminComment)"
    }
}