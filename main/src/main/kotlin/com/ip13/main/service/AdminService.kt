package com.ip13.main.service

import com.ip13.main.model.entity.Admin
import com.ip13.main.repository.AdminRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminRepository: AdminRepository
) {
    fun getActiveByUserIdOrNull(userId: Int): Admin? {
        return adminRepository.getActiveByUserIdOrNull(userId)
    }
}