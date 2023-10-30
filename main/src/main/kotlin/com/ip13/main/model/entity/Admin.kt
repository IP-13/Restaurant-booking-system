package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.*

@Entity
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User = User(),
)