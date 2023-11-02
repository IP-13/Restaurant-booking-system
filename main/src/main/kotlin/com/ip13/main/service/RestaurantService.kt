package com.ip13.main.service

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.AddressRepository
import com.ip13.main.repository.RestaurantRepository
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val addressService: AddressService,
) {
    fun save(restaurant: Restaurant): Int {
        addressService.save(restaurant.address)
        return restaurantRepository.save(restaurant).id
    }
}