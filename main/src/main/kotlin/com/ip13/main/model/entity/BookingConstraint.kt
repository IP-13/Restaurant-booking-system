package com.ip13.main.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BookingConstraint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    val manager: Manager,
    // TODO()
    // может ли быть несколько ограничений на одну заявку бронирования столика
    @OneToOne
    @JoinColumn(name = "reserve_table_ticket_id")
    val reserveTableTicket: ReserveTableTicket,
    val reason: String,
    @Column(name = "creation_date")
    val creationDate: LocalDateTime,
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime,
)