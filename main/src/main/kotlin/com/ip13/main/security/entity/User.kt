package com.ip13.main.security.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ip13.main.model.entity.*
import com.ip13.main.model.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user_t")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    // поля private, чтобы можно было без ошибок переопределить методы getPassword, getUsername
    private val username: String = "",
    private val password: String = "",
    val numOfGrades: Int = 0,
    val sumOfGrades: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val roles: MutableList<Role> = mutableListOf(),
    // Сушности, которые ссылаются на user_t
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val restaurantAddTicketsAsUser: MutableList<RestaurantAddTicket> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
    val restaurantAddTicketsAsAdmin: MutableList<RestaurantAddTicket> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    val restaurants: MutableList<Restaurant> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val tableReserveTicketsAsUser: MutableList<TableReserveTicket> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    val tableReserveTicketsAsManager: MutableList<TableReserveTicket> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val gradesToRestaurants: MutableList<GradeVisitor> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val gradesFromManagers: MutableList<GradeManager> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    val gradesAsManager: MutableList<GradeManager> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    val createdBookingConstraints: MutableList<BookingConstraint> = mutableListOf(),
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val blackListEntries: MutableList<BlackList> = mutableListOf(),
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return username == other.username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password', numOfGrades=$numOfGrades, " +
                "sumOfGrades=$sumOfGrades, roles=$roles)"
    }
}