package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.RestaurantAddResult
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "restaurant_add_ticket_id")
    val restaurantAddTicketId: Int,
    @Column(name = "admin_id")
    val adminId: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val result: RestaurantAddResult = RestaurantAddResult.PROCESSING,
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now(),
    val adminComment: String? = null,
)