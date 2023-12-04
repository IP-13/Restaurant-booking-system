package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.RestaurantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    fun save(restaurant: Restaurant): Restaurant {
        return restaurantRepository.save(restaurant)
    }

    fun findByIdOrNull(id: Int): Restaurant? {
        return restaurantRepository.findByIdOrNull(id)
    }

    fun findByIdOrThrow(id: Int): Restaurant {
        return restaurantRepository.findByIdOrNull(id) ?: throw RestaurantNotFoundException("No restaurant with id $id")
    }

    fun findByManagerIdOrThrow(managerId: Int): Restaurant {
        return restaurantRepository.findByManagerId(managerId)
            ?: throw RestaurantNotFoundException("No restaurant with manager with id $managerId")
    }
}