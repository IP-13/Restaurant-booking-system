package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.RestaurantAddResult
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RestaurantAddTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val restaurantAddTicketId: Int = 0,
    val adminId: Int = 0,
    @Enumerated(value = EnumType.STRING)
    val result: RestaurantAddResult = RestaurantAddResult.PROCESSING,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val adminComment: String? = null,
)