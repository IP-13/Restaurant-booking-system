package com.ip13.main.model.entity

import com.ip13.main.model.entity.enums.RestaurantAddResult
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class RestaurantAddTicketResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "admin_id")
    val adminId: Int = 0,
    val result: RestaurantAddResult = RestaurantAddResult.REJECTED,
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now(),
    val adminComment: String? = null,
)