package com.ip13.main.service

import com.ip13.main.repository.ManagerRepository
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val managerRepository: ManagerRepository,
) {
    fun save(manager: Manager): Int {
        return managerRepository.save(manager).id
    }

    fun checkIfWorksInRestaurantById(managerId: Int, restaurantId: Int): Boolean {
        return managerRepository.checkIfWorksInRestaurantById(managerId, restaurantId)
    }

    fun checkIfActive(managerId: Int): Boolean {
        return managerRepository.checkIfActive(managerId)
    }

    fun getManagerByUserIdOrNull(userId: Int): Manager? {
        return managerRepository.getManagerByUserId(userId)
    }
}