package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.request.RestaurantAddTicketRequestDto
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequestDto
import com.ip13.main.model.dto.request.RoleAddRequestDto
import com.ip13.main.model.dto.response.RestaurantAddTicketResponseDto
import com.ip13.main.model.dto.response.RestaurantProcessTicketResponseDto
import com.ip13.main.model.entity.Restaurant
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
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

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
     * @throws RestaurantAddTicketNotFoundException if RestaurantAddTicket with that id doesn't exist
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
        dto: RestaurantProcessTicketRequestDto,
    ): RestaurantProcessTicketResponseDto {
        val restaurantAddTicket = findByIdOrThrow(dto.restaurantAddTicketId)

        log.debug("Restaurant add ticket found\n{}", restaurantAddTicket.toString())

        if (restaurantAddTicket.status != RestaurantAddStatus.PROCESSING) {
            throw CommonException(
                "Ticket with id ${dto.restaurantAddTicketId} already processed. Status ${restaurantAddTicket.status}",
                HttpStatus.BAD_REQUEST
            )
        }

        val admin = userService.getUserByTokenInHeader(authHeader)

        log.debug("Admin found\n{}", admin.toString())

        val updatedRestaurantAddTicket = restaurantAddTicket.updateRestaurantAddTicket(
            status = dto.status,
            admin = admin,
            adminComment = dto.adminComment,
        )

        if (dto.status == RestaurantAddStatus.ACCEPTED) {
            val restaurant = updatedRestaurantAddTicket.toRestaurant()

            val restaurantId = saveRestaurantAddTicketAndRestaurantTransactional(updatedRestaurantAddTicket, restaurant)

            return RestaurantProcessTicketResponseDto(RestaurantAddStatus.ACCEPTED, restaurantId)
        }

        save(updatedRestaurantAddTicket)

        return RestaurantProcessTicketResponseDto(dto.status, null)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private fun saveRestaurantAddTicketAndRestaurantTransactional(
        updatedRestaurantAddTicket: RestaurantAddTicket,
        restaurant: Restaurant,
    ): Int {
        save(updatedRestaurantAddTicket)
        userService.addRole(RoleAddRequestDto(updatedRestaurantAddTicket.user.id, Role.MANAGER))
        return restaurantService.save(restaurant).id
    }

    fun getTickets(pageRequest: PageRequest): List<RestaurantAddTicket> {
        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}