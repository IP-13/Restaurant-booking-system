package com.ip13.main.model.entity

import com.ip13.main.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class BlackList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    // TODO()
    // можно ли одного человека несколько раз внести в черный список
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    val manager: Manager,
    @Column(name = "from_date")
    val fromDate: LocalDateTime,
    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime,
    val reason: String?,
)