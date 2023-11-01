package com.ip13.main.service

import com.ip13.main.repository.RestaurantRepository
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    val restaurantRepository: RestaurantRepository,
) {

}