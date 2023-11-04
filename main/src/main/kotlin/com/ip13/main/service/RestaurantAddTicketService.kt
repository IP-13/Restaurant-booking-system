package com.ip13.main.service

import com.ip13.main.mapper.RestaurantMapper
import com.ip13.main.model.entity.Manager
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.RestaurantAddTicketResult
import com.ip13.main.model.entity.enums.RestaurantAddStatus
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.security.entity.User
import com.ip13.main.security.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantAddTicketResultService: RestaurantAddTicketResultService,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
    private val managerService: ManagerService,
) {
    fun save(restaurantAddTicket: RestaurantAddTicket) {
        restaurantAddTicketRepository.save(restaurantAddTicket)
    }

    fun findByIdOrNull(id: Int): RestaurantAddTicket? {
        return restaurantAddTicketRepository.findByIdOrNull(id)
    }

    fun processRestaurantAddTicket(result: RestaurantAddTicketResult, ticket: RestaurantAddTicket): Int? {
        restaurantAddTicketResultService.save(result)

        return if (result.result == RestaurantAddStatus.ACCEPTED) {
            val restaurant = RestaurantMapper.fromRestaurantAddTicket(ticket)
            userService.addRole(ticket.userId, Role.MANAGER.name)
            // need to save restaurant before saving manager, because manager references on restaurant table
            val newRestaurantId = restaurantService.save(restaurant)
            managerService.save(
                Manager(
                    userId = ticket.userId,
                    restaurantId = newRestaurantId,
                )
            )
            // return restaurant id
            newRestaurantId
        } else {
            null
        }
    }

    fun getTickets(pageRequest: PageRequest): List<RestaurantAddTicket> {
        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}