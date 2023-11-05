package com.ip13.main.service

import com.ip13.main.mapper.RestaurantMapper
import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.dto.toAddress
import com.ip13.main.model.entity.*
import com.ip13.main.model.entity.enums.RestaurantAddStatus
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.security.entity.User
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantAddTicketResultService: RestaurantAddTicketResultService,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
    private val managerService: ManagerService,
    private val addressService: AddressService,
    private val transactionManager: PlatformTransactionManager,
) {
    fun save(restaurantAddTicket: RestaurantAddTicket): Int {
        return restaurantAddTicketRepository.save(restaurantAddTicket).id
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
                    isActive = true,
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

    fun saveTransactionalWithAddress(
        restaurantAddTicketDto: RestaurantAddTicketDto,
        authHeader: String,
    ): Int? {
        val user = userService.getUserByTokenInHeader(authHeader)

        val address = restaurantAddTicketDto.addressDto.toAddress()

        val transactionTemplate = TransactionTemplate(transactionManager)

        val restaurantAddTicketId = transactionTemplate.execute {
            val addressId = addressService.save(address)

            val restaurantAddTicket = RestaurantAddTicket(
                name = restaurantAddTicketDto.name,
                description = restaurantAddTicketDto.description,
                addressId = addressId,
                userId = user.id,
                creationDate = LocalDateTime.now(),
            )

            save(restaurantAddTicket)
        }

        return restaurantAddTicketId
    }
}