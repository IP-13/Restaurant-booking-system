package com.ip13.main.handler

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.RestaurantRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RestaurantHandler(
    private val restaurantRepository: RestaurantRepository,
) {
    fun findById(id: Int): Mono<Restaurant> {
        return restaurantRepository.findById(id)
    }

    fun addGrade(id: Int, grade: Int): Mono<Int> {
        return restaurantRepository.addGrade(id, grade)
    }

    fun save(restaurant: Restaurant): Mono<Restaurant> {
        return restaurantRepository.save(restaurant)
    }

    fun getGrade(id: Int): Mono<Float> {
        return restaurantRepository.getGrade(id)
    }
}