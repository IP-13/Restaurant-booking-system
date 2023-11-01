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
    // TODO() default user rating
    private val rating: Float = 5.0f,
    @Enumerated(value = EnumType.STRING)
    val roles: List<Role> = listOf(),
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.code) }.toMutableList()
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
}