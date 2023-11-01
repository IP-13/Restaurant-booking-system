package com.ip13.main.service

import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.repository.RestaurantAddTicketRepository
import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketService(
    val restaurantAddTicketRepository: RestaurantAddTicketRepository,
) {
    fun save(restaurantAddTicket: RestaurantAddTicket) {
        restaurantAddTicketRepository.save(restaurantAddTicket)
    }
}