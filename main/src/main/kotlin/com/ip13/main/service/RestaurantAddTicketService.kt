package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.AdminNotFoundException
import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.dto.RestaurantAddTicketResultDto
import com.ip13.main.model.entity.Manager
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.RestaurantAddTicketResult
import com.ip13.main.model.entity.enums.RestaurantAddStatus
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.model.toAddress
import com.ip13.main.model.toRestaurant
import com.ip13.main.repository.RestaurantAddTicketRepository
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
    private val adminService: AdminService,
    private val transactionManager: PlatformTransactionManager,
) {
    private val log = getLogger(javaClass)

    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun save(restaurantAddTicket: RestaurantAddTicket): Int {
        return restaurantAddTicketRepository.save(restaurantAddTicket).id
    }

    fun findByIdOrNull(id: Int): RestaurantAddTicket? {
        return restaurantAddTicketRepository.findByIdOrNull(id)
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

        val admin = adminService.getActiveByUserIdOrNull(user.id)
            ?: throw AdminNotFoundException("No active admin found with user_id ${user.id}")

        log.debug("Admin found\n{}", admin.toString())

        val restaurantAddTicketResult = RestaurantAddTicketResult(
            restaurantAddTicketId = dto.restaurantAddTicketId,
            result = dto.result,
            adminId = admin.id,
            creationDate = LocalDateTime.now(),
            adminComment = dto.adminComment,
        )

        val restaurant = restaurantAddTicket.toRestaurant()

        return if (restaurantAddTicketResult.result == RestaurantAddStatus.ACCEPTED) {
            transactionTemplate.execute {
                restaurantAddTicketResultService.save(restaurantAddTicketResult)
                userService.addRole(restaurantAddTicket.userId, Role.MANAGER.name)
                // need to save restaurant before saving manager, because manager references on restaurant table
                val newRestaurantId = restaurantService.save(restaurant)
                managerService.save(
                    Manager(
                        // id пользователя, который создал заявку на добавление ресторана
                        userId = restaurantAddTicket.userId,
                        restaurantId = newRestaurantId,
                        isActive = true,
                    )
                )
                log.debug("New restaurant id {}", newRestaurantId)
                // return restaurant id
                newRestaurantId
            }
        } else {
            restaurantAddTicketResultService.save(restaurantAddTicketResult)
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

        log.debug("User found\n{}", user.toString())

        val address = restaurantAddTicketDto.addressDto.toAddress()

        log.debug("Address found\n{}", address.toString())
        
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