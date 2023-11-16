package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.request.RoleAddRequest
import com.ip13.main.model.dto.response.RestaurantCreateTicketResponse
import com.ip13.main.model.dto.response.RestaurantProcessTicketResponse
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
import org.springframework.data.domain.Sort
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

    fun createTicket(authHeader: String, request: RestaurantAddTicketRequest): RestaurantCreateTicketResponse {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("User found\n{}", user.toString())

        val restaurantAddTicket = save(request.toRestaurantAddTicket(user, RestaurantAddStatus.PROCESSING))

        return RestaurantCreateTicketResponse(restaurantAddTicket.status)
    }

    fun processRestaurantAddTicket(
        authHeader: String,
        request: RestaurantProcessTicketRequest,
    ): RestaurantProcessTicketResponse {
        val restaurantAddTicket = findByIdOrThrow(request.restaurantAddTicketId)

        log.debug("Restaurant add ticket found\n{}", restaurantAddTicket.toString())

        if (restaurantAddTicket.status != RestaurantAddStatus.PROCESSING) {
            throw CommonException(
                "Ticket with id ${request.restaurantAddTicketId} already processed. Status ${restaurantAddTicket.status}",
                HttpStatus.BAD_REQUEST
            )
        }

        val admin = userService.getUserByTokenInHeader(authHeader)

        log.debug("Admin found\n{}", admin.toString())

        val processedRestaurantAddTicket = restaurantAddTicket.updateRestaurantAddTicket(
            status = request.status,
            admin = admin,
            adminComment = request.adminComment,
        )

        if (request.status == RestaurantAddStatus.ACCEPTED) {
            val restaurant = processedRestaurantAddTicket.toRestaurant()

            val restaurantId = saveRestaurantAddTicketAndRestaurantTransactional(processedRestaurantAddTicket, restaurant)

            return RestaurantProcessTicketResponse(RestaurantAddStatus.ACCEPTED, restaurantId)
        }

        save(processedRestaurantAddTicket)

        return RestaurantProcessTicketResponse(request.status, null)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private fun saveRestaurantAddTicketAndRestaurantTransactional(
        updatedRestaurantAddTicket: RestaurantAddTicket,
        restaurant: Restaurant,
    ): Int {
        save(updatedRestaurantAddTicket)
        userService.addRole(RoleAddRequest(updatedRestaurantAddTicket.user.id, Role.MANAGER))
        return restaurantService.save(restaurant).id
    }

    fun getTickets(pageNumber: Int, pageSize: Int): List<RestaurantAddTicket> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}