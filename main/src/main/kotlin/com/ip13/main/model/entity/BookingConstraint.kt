package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BookingConstraint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant = Restaurant(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    val manager: Manager = Manager(),
    val reason: String = "",
    @Column(name = "creation_date")
    val creationDate: LocalDateTime = LocalDateTime.now(),
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime = LocalDateTime.now(),
)