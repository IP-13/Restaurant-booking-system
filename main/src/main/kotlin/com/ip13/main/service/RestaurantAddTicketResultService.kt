package com.ip13.main.service

import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketResultService(
    private val restaurantAddTicketResultRepository: RestaurantAddTicketResultRepository,
) {
    fun save(restaurantAddTicketResult: RestaurantAddTicketResult): Int {
        return restaurantAddTicketResultRepository.save(restaurantAddTicketResult).id
    }
}