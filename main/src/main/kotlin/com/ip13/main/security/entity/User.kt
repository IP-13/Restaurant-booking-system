package com.ip13.main.security.entity

import com.ip13.main.model.entity.enums.Role
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
    private val username: String = "",
    private val password: String = "",
    private val numOfGrades: Int = 0,
    private val sumOfGrades: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val roles: List<Role> = listOf(),
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

        if (username != other.username) return false
        if (password != other.password) return false
        if (numOfGrades != other.numOfGrades) return false
        if (sumOfGrades != other.sumOfGrades) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + numOfGrades
        result = 31 * result + sumOfGrades
        result = 31 * result + roles.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password', numOfGrades=$numOfGrades, sumOfGrades=$sumOfGrades, roles=$roles)"
    }
}