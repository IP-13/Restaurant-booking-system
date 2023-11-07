package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BlackList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user: User = User(),
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val tillDate: LocalDateTime = LocalDateTime.now(),
    val reason: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlackList

        if (user != other.user) return false
        if (fromDate != other.fromDate) return false
        if (tillDate != other.tillDate) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + tillDate.hashCode()
        result = 31 * result + (reason?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BlackList(id=$id, user=$user, fromDate=$fromDate, tillDate=$tillDate, reason=$reason)"
    }
}