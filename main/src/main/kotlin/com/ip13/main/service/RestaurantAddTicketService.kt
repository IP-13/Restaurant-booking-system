package com.ip13.main.service

import com.ip13.main.mapper.RestaurantMapper
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.RestaurantAddTicketResult
import com.ip13.main.model.entity.enums.RestaurantAddResult
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.repository.RestaurantAddTicketResultRepository
import com.ip13.main.security.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantAddTicketResultRepository: RestaurantAddTicketResultRepository,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
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
            userService.addRole(ticket.userId, Role.MANAGER.name)
            restaurantService.save(restaurant)
        } else {
            null
        }
    }

    fun getTickets(pageRequest: PageRequest): List<RestaurantAddTicket> {
        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}