package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.request.RestaurantAddTicketRequestDto
import com.ip13.main.model.dto.request.RestaurantAddTicketResultDto
import com.ip13.main.model.dto.response.RestaurantAddTicketResponseDto
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.toRestaurantAddTicket
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
    private val transactionManager: PlatformTransactionManager,
) {
    private val log = getLogger(javaClass)

    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun save(restaurantAddTicket: RestaurantAddTicket): RestaurantAddTicket {
        return restaurantAddTicketRepository.save(restaurantAddTicket)
    }

    fun findByIdOrNull(id: Int): RestaurantAddTicket? {
        return restaurantAddTicketRepository.findByIdOrNull(id)
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
        val restaurantAddTicket = findByIdOrNull(dto.restaurantAddTicketId)
            ?: throw RestaurantAddTicketNotFoundException("No restaurantAddTicket with id ${dto.restaurantAddTicketId}")

        log.debug("Restaurant add ticket found\n{}", restaurantAddTicket.toString())

        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("User found\n{}", user.toString())

//        if (!user.roles.contains(Role.ADMIN)) {
//            // TODO(throw NotAdminException)
//        }
//
//        val restaurant = restaurantAddTicket.toRestaurant()
//
//        return if (restaurantAddTicketResult.result == RestaurantAddStatus.ACCEPTED) {
//            transactionTemplate.execute {
//                restaurantAddTicketResultService.save(restaurantAddTicketResult)
//                userService.addRole(restaurantAddTicket.userId, Role.MANAGER.name)
//                // need to save restaurant before saving manager, because manager references on restaurant table
//                val newRestaurantId = restaurantService.save(restaurant)
//                managerService.save(
//                    Manager(
//                        // id пользователя, который создал заявку на добавление ресторана
//                        userId = restaurantAddTicket.userId,
//                        restaurantId = newRestaurantId,
//                        isActive = true,
//                    )
//                )
//                log.debug("New restaurant id {}", newRestaurantId)
//                // return restaurant id
//                newRestaurantId
//            }
//        } else {
//            restaurantAddTicketResultService.save(restaurantAddTicketResult)
//            null
//        }

        return null
    }

    fun getTickets(pageRequest: PageRequest): List<RestaurantAddTicket> {
        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }
}