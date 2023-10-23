package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.ReserveTableStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class AddRestaurantTicket(
    @Id
    val id: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    val manager: Manager,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    val admin: Admin,
    // TODO()
    // Надо подумать можно ли несколько заявок создавать на добавление одного ресторана
    // и нужно ли удалять ресторан из базы, если заявку на его добавление отклонили, потому что в нынешней реализации
    // сначала в базу добавляется ресторан, а только потом заявка на его создание
    @OneToOne
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant,
    val status: ReserveTableStatus,
    @Column(name = "create_date")
    val createDate: LocalDateTime,
    @Column(name = "last_status_update")
    val lastStatusUpdate: LocalDateTime,
    @Column(name = "author_comment")
    val authorComment: String?,
    @Column(name = "admin_comment")
    val adminComment: String?,
)