package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "add_restaurant_ticket")
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
    @Column(name = "user_id")
    val userId: Int = 0,
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now(),
)