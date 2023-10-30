package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BlackList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    val manager: Manager = Manager(),
    @Column(name = "from_date")
    val fromDate: LocalDateTime = LocalDateTime.now(),
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime = LocalDateTime.now(),
    val reason: String? = null,
)