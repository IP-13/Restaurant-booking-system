package com.ip13.main.service

import com.ip13.main.mapper.RestaurantMapper
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.RestaurantAddTicketResult
import com.ip13.main.model.entity.enums.RestaurantAddResult
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.repository.AddressRepository
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.repository.RestaurantAddTicketResultRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.security.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketService(
    val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    val restaurantAddTicketResultRepository: RestaurantAddTicketResultRepository,
    val restaurantService: RestaurantService,
    // TODO() is it ok to add role from repository
    val userRepository: UserRepository,
) {
    fun save(restaurantAddTicket: RestaurantAddTicket) {
        restaurantAddTicketRepository.save(restaurantAddTicket)
    }

    fun findByIdOrNull(id: Int): RestaurantAddTicket? {
        return restaurantAddTicketRepository.findByIdOrNull(id)
    }

    fun processRestaurantAddTicket(result: RestaurantAddTicketResult, ticket: RestaurantAddTicket): Int? {
        restaurantAddTicketResultRepository.save(result)

        return if (result.result == RestaurantAddResult.ACCEPTED) {
            val restaurant = RestaurantMapper.restaurantFromRestaurantAddTicket(ticket)
            userRepository.addRole(ticket.userId, Role.MANAGER.name)
            restaurantService.save(restaurant)
        } else {
            null
        }
    }
}