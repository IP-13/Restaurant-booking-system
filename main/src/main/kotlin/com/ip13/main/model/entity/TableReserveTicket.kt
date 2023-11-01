package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.ReserveTableStatus
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
    @ManyToOne
    @JoinColumn(name = "manager_id")
    val manager: Manager = Manager(),
    @Enumerated(value = EnumType.STRING)
    val status: ReserveTableStatus = ReserveTableStatus.PROCESSING,
    @Column(name = "creation_date")
    val creationDate: LocalDateTime = LocalDateTime.now(),
    @Column(name = "user_comment")
    val visitorComment: String? = null,
    @Column(name = "manager_comment")
    val managerComment: String? = null,
)