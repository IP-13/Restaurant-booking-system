package com.ip13.main.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class MyEntity(
    @Id
    val a: Int,
    val b: Int,
)