package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val addressId: Int = 0,
    val restaurantAddTicketId: Int = 0,
    val description: String? = null,
    val numOfGrades: Int = 0,
    val sumOfGrades: Int = 0,
)