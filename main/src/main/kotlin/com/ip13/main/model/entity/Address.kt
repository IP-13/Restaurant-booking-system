package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Address(
    @Id
    val id: Int,
    val country: String,
    val city: String,
    val street: String,
    val building: Int,
    val entrance: Int?,
    val floor: Int?,
)