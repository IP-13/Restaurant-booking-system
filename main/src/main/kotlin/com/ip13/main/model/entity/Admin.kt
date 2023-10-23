package com.ip13.main.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Admin(
    @Id
    val id: Int,
    @OneToOne
    @JoinColumn(name = "person_id")
    val person: Person,
)