package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.request.RestaurantAddTicketRequestDto
import com.ip13.main.model.dto.request.RestaurantAddTicketResultDto
import com.ip13.main.model.dto.request.RoleAddDto
import com.ip13.main.model.dto.response.RestaurantAddTicketResponseDto
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.enums.Role
import com.ip13.main.model.toRestaurant
import com.ip13.main.model.toRestaurantAddTicket
import com.ip13.main.model.updateRestaurantAddTicket
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
) {
    private val log = getLogger(javaClass)

    fun save(restaurantAddTicket: RestaurantAddTicket): RestaurantAddTicket {
        return restaurantAddTicketRepository.save(restaurantAddTicket)
    }

    fun findByIdOrNull(id: Int): RestaurantAddTicket? {
        return restaurantAddTicketRepository.findByIdOrNull(id)
    }

    /**
     * throw RestaurantAddTicketNotFoundException if RestaurantAddTicket with that id doesn't exist
     */
    fun findByIdOrThrow(id: Int): RestaurantAddTicket {
        return restaurantAddTicketRepository.findByIdOrNull(id)
            ?: throw RestaurantAddTicketNotFoundException("No restaurantAddTicket with id $id")
    }

    fun createTicket(authHeader: String, dto: RestaurantAddTicketRequestDto): RestaurantAddTicketResponseDto {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("User found\n{}", user.toString())

        val restaurantAddTicket = save(dto.toRestaurantAddTicket(user, RestaurantAddStatus.PROCESSING))

        return RestaurantAddTicketResponseDto(restaurantAddTicket.status)
    }

    fun processRestaurantAddTicket(
        authHeader: String,
        dto: RestaurantAddTicketResultDto,
    ): Int? {
        val restaurantAddTicket = findByIdOrThrow(dto.restaurantAddTicketId)

        log.debug("Restaurant add ticket found\n{}", restaurantAddTicket.toString())

        val admin = userService.getUserByTokenInHeader(authHeader)

        log.debug("Admin found\n{}", admin.toString())

        val updatedRestaurantAddTicket = restaurantAddTicket.updateRestaurantAddTicket(
            status = dto.status,
            admin = admin,
            adminComment = dto.adminComment,
        )

        if (dto.status == RestaurantAddStatus.ACCEPTED) {
            val restaurant = updatedRestaurantAddTicket.toRestaurant()

            save(updatedRestaurantAddTicket)
            userService.addRole(RoleAddDto(updatedRestaurantAddTicket.user.id, Role.MANAGER))
            restaurantService.save(restaurant)

        } else if (dto.status == RestaurantAddStatus.REJECTED) {
            save(updatedRestaurantAddTicket)
        }

        return null
    }

    fun getTickets(pageRequest: PageRequest): List<RestaurantAddTicket> {
        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}