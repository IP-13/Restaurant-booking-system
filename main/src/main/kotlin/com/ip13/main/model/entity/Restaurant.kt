package com.ip13.main.model.entity

import jakarta.persistence.*

@Entity
class Restaurant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val name: String,
    @OneToOne
    @JoinColumn(name = "address_id")
    val address: Address,
    val description: String?,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    val managers: List<Manager> = emptyList()
)