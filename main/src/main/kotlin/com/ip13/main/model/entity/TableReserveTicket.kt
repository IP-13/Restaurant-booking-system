package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class TableReserveTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val userComment: String? = null,
)