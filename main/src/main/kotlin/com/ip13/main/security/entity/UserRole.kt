package com.ip13.main.security.entity

import com.ip13.main.model.entity.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "user_role")
class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    @Enumerated(EnumType.STRING)
    val role: Role = Role.VISITOR,
)