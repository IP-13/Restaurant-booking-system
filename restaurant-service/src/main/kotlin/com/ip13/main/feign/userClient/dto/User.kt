package com.ip13.main.feign.userClient.dto

import com.ip13.main.model.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val numOfGrades: Int,
    val sumOfGrades: Int,
    val roles: List<Role> = listOf(),
)