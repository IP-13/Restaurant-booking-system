package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val addressId: Int = 0,
    val description: String? = null,
    val userId: Int = 0,
    val createDate: LocalDateTime = LocalDateTime.now(),
)