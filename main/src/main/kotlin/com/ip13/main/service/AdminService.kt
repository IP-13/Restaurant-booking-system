package com.ip13.main.service

import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {
    fun getActiveByUserIdOrNull(userId: Int): Admin? {
        return adminRepository.getActiveByUserIdOrNull(userId)
    }
}