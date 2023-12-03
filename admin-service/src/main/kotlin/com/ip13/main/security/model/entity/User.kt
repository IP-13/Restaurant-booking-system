package com.ip13.main.security.model.entity

import com.ip13.main.security.model.enums.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

//@Entity
//@Table(name = "user_t")
class User(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    // поля private, чтобы можно было без ошибок переопределить методы getPassword, getUsername
    private val username: String = "",
    private val password: String = "",
    val numOfGrades: Int = 0,
    val sumOfGrades: Int = 0,
//    @Enumerated(value = EnumType.STRING)
    val roles: List<Role> = listOf(),
    // Сушности, которые ссылаются на user_t
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