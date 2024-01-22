package com.ip13.main.service

import com.ip13.main.event.RestaurantCreatedEvent
import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.feign.userClient.dto.RoleAddRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.response.RestaurantAddTicketResponse
import com.ip13.main.model.dto.response.RestaurantProcessTicketResponse
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.enums.Role
import com.ip13.main.model.toRestaurant
import com.ip13.main.model.toRestaurantAddTicket
import com.ip13.main.model.updateRestaurantAddTicket
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.util.getLogger
import feign.FeignException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantAddTicketService(
    private val restaurantAddTicketRepository: RestaurantAddTicketRepository,
    private val restaurantService: RestaurantService,
    private val userClient: UserClient,
    private val kafkaTemplate: KafkaTemplate<String, RestaurantCreatedEvent>,
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

    fun createTicket(
        request: RestaurantAddTicketRequest,
        username: String,
    ): RestaurantAddTicketResponse {
        val restaurantAddTicket = save(request.toRestaurantAddTicket(username, RestaurantAddStatus.PROCESSING))

        return RestaurantAddTicketResponse(restaurantAddTicket.status)
    }

    fun processRestaurantAddTicket(
        request: RestaurantProcessTicketRequest,
        adminName: String,
        authHeader: String,
    ): RestaurantProcessTicketResponse {
        val restaurantAddTicket = findByIdOrThrow(request.restaurantAddTicketId)

        log.debug("Restaurant add ticket found\n{}", restaurantAddTicket.toString())

        if (restaurantAddTicket.status != RestaurantAddStatus.PROCESSING) {
            throw CommonException(
                "Ticket with id ${request.restaurantAddTicketId} already processed. Status ${restaurantAddTicket.status}",
                HttpStatus.BAD_REQUEST
            )
        }

        val processedRestaurantAddTicket = restaurantAddTicket.updateRestaurantAddTicket(
            status = request.status,
            adminName = adminName,
            adminComment = request.adminComment,
        )

        if (request.status == RestaurantAddStatus.ACCEPTED) {
            val restaurant = processedRestaurantAddTicket.toRestaurant()

            try {
                val restaurantId =
                    saveRestaurantAddTicketAndRestaurantTransactional(processedRestaurantAddTicket, restaurant)

                log.debug("after saving restaurant and updated restaurant add ticket. Restaurant id: {}", restaurantId)

                kafkaTemplate.send(
                    RESTAURANT_CREATED_TOPIC,
                    RestaurantCreatedEvent(restaurantId, processedRestaurantAddTicket.username)
                )

                return RestaurantProcessTicketResponse(RestaurantAddStatus.ACCEPTED, restaurantId)
            } catch (_: FeignException) {
                log.debug("no response received from user-service")

                return RestaurantProcessTicketResponse(RestaurantAddStatus.TRY_ONE_MORE_TIME, -1)
            }
        }

        // if status not ACCEPTED, then just save ticket
        save(processedRestaurantAddTicket)

        return RestaurantProcessTicketResponse(request.status, null)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private fun saveRestaurantAddTicketAndRestaurantTransactional(
        updatedRestaurantAddTicket: RestaurantAddTicket,
        restaurant: Restaurant,
    ): Int {
        save(updatedRestaurantAddTicket)
        return restaurantService.save(restaurant).id
    }

    fun getTickets(pageNumber: Int, pageSize: Int): List<RestaurantAddTicket> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        return restaurantAddTicketRepository.findAll(pageRequest).toList()
    }

    companion object {
        private const val RESTAURANT_CREATED_TOPIC = "restaurant-created"
    }
}