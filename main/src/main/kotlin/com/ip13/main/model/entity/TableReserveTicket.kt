package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.ReserveTableStatus
import com.ip13.main.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class TableReserveTicket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "manager_id")
    val manager: Manager,
    val status: ReserveTableStatus,
    @Column(name = "creation_date")
    val creationDate: LocalDateTime,
    @Column(name = "last_status_update")
    val lastStatusUpdate: LocalDateTime,
    @Column(name = "visitor_comment")
    val visitorComment: String?,
    @Column(name = "manager_comment")
    val managerComment: String?,
)