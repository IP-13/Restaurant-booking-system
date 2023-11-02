package com.ip13.main.service

import com.ip13.main.model.entity.Manager
import com.ip13.main.repository.ManagerRepository
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val managerRepository: ManagerRepository,
) {
    fun save(manager: Manager): Int {
        return managerRepository.save(manager).id
    }
}