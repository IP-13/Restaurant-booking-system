package com.ip13.main.handler

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.RestaurantCoRepository
import org.springframework.stereotype.Component

@Component
class RestaurantCoHandler(
    private val restaurantCoRepository: RestaurantCoRepository,
) {
    suspend fun findByRestaurantIdOrNull(restaurantId: Int): Restaurant? {
        return restaurantCoRepository.findByRestaurantId(restaurantId)
    }

    suspend fun addGrade(id: Int, grade: Int): Int {
        return restaurantCoRepository.addGrade(id, grade)
    }

    suspend fun save(restaurant: Restaurant): Restaurant {
        return restaurantCoRepository.save(restaurant)
    }

    suspend fun getGrade(id: Int): Float {
        return restaurantCoRepository.getGrade(id)
    }
}