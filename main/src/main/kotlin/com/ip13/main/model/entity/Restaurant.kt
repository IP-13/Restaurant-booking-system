package com.ip13.main.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ip13.main.security.entity.User
import jakarta.persistence.*

@Entity
class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne
    @JoinColumn(name = "restaurant_add_ticket_id")
    val restaurantAddTicket: RestaurantAddTicket = RestaurantAddTicket(),
    @ManyToOne
    @JoinColumn(name = "manager_id")
    val manager: User = User(),
    val name: String = "",
    val country: String = "",
    val city: String = "",
    val street: String = "",
    val building: Int = 0,
    val entrance: Int? = null,
    val floor: Int? = null,
    val description: String? = null,
    val numOfGrades: Int = 0,
    val sumOfGrades: Int = 0,
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    val tableReserveTickets: List<TableReserveTicket> = listOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    val gradesFromVisitors: List<GradeVisitor> = listOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    val bookingConstraints: List<BookingConstraint> = listOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (restaurantAddTicket != other.restaurantAddTicket) return false
        if (manager != other.manager) return false
        if (name != other.name) return false
        if (country != other.country) return false
        if (city != other.city) return false
        if (street != other.street) return false
        if (building != other.building) return false
        if (entrance != other.entrance) return false
        if (floor != other.floor) return false
        if (description != other.description) return false
        if (numOfGrades != other.numOfGrades) return false
        if (sumOfGrades != other.sumOfGrades) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantAddTicket.hashCode()
        result = 31 * result + manager.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + building
        result = 31 * result + (entrance ?: 0)
        result = 31 * result + (floor ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + numOfGrades
        result = 31 * result + sumOfGrades
        return result
    }

    override fun toString(): String {
        return "Restaurant(id=$id, restaurantAddTicket=$restaurantAddTicket, manager=$manager, name='$name', " +
                "country='$country', city='$city', street='$street', building=$building, entrance=$entrance, " +
                "floor=$floor, description=$description, numOfGrades=$numOfGrades, sumOfGrades=$sumOfGrades)"
    }
}