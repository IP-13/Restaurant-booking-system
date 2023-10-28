package com.ip13.main.model.entity

import jakarta.persistence.*

@Entity
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @OneToOne
    @JoinColumn(name = "person_id")
    val person: Person,
)