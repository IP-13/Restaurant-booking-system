package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.ReserveTableStatus
import jakarta.persistence.*

@Entity
class TableReserveTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val managerId: Int = 0,
    val managerComment: String? = null,
    @Enumerated(value = EnumType.STRING)
    val status: ReserveTableStatus = ReserveTableStatus.PROCESSING,
)