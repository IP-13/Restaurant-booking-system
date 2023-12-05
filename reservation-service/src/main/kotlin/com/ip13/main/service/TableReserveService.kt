package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
import com.ip13.main.feign.restaurantClient.RestaurantClient
import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.ReservationProcessResponse
import com.ip13.main.model.dto.response.ShowReservationsResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.model.toTableReserveTicket
import com.ip13.main.model.toTableReserveTicketResponse
import com.ip13.main.repository.TableReserveTicketRepository
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TableReserveService(
    private val tableReserveTicketRepository: TableReserveTicketRepository,
    private val userClient: UserClient,
    private val restaurantClient: RestaurantClient,
) {
    private val log = getLogger(javaClass)

    fun save(tableReserveTicket: TableReserveTicket): TableReserveTicket {
        return tableReserveTicketRepository.save(tableReserveTicket)
    }

    fun getReservations(pageNumber: Int, pageSize: Int, username: String): ShowReservationsResponse {
        val manager = userClient.getUserByUsername(username)

        log.debug("manager extracted from token\n{}", manager.toString())

        val restaurant = restaurantService.findByManagerIdOrThrow(manager.id)

        log.debug("restaurant found by manager id\n{}", restaurant.toString())

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val reservations =
            tableReserveTicketRepository.findAll(pageRequest).filter { it.restaurant.id == restaurant.id }.toList()

        log.debug("reservations found\n{}", reservations.map { it.toString() })

        return ShowReservationsResponse(reservations.map { it.toTableReserveTicketResponse() })
    }

    fun findByIdOrNull(id: Int): TableReserveTicket? {
        return tableReserveTicketRepository.findByIdOrNull(id)
    }

    /**
     * @throws TableReserveTicketNotFoundException if TableReserveTicket with that id doesn't exist
     */
    fun findByIdOrThrow(id: Int): TableReserveTicket {
        return tableReserveTicketRepository.findByIdOrNull(id)
            ?: throw TableReserveTicketNotFoundException("No table reserve ticket with id $id")
    }

    fun reserveTable(request: TableReserveRequest, username: String): TableReserveResponse {
        val user = userService.loadUserByUsername(username)

        log.debug("user extracted from token\n{}", user.toString())

        val restaurant = restaurantService.findByIdOrThrow(request.restaurantId)

        if (user.blackListEntries.any {
                it.tillDate.isAfter(LocalDateTime.now())
            }) {
            save(
                request.toTableReserveTicket(
                    restaurant = restaurant,
                    user = user,
                    managerComment = "You're in a black list for bad behaviour",
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponse(
                status = TableReserveStatus.REJECTED,
            )

        }

        val bookingConstraints = restaurant.bookingConstraints.filter {
            request.fromDate < it.tillDate && request.fromDate >= it.fromDate ||
                    request.tillDate <= it.tillDate && request.tillDate > it.fromDate
        }

        if (bookingConstraints.isNotEmpty()) {
            save(
                request.toTableReserveTicket(
                    restaurant = restaurant,
                    user = user,
                    managerComment = "Sorry, restaurant ${restaurant.id} is closed at that time",
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponse(
                status = TableReserveStatus.REJECTED,
            )
        }

        save(
            request.toTableReserveTicket(
                restaurant = restaurant,
                user = user,
                managerComment = null,
                status = TableReserveStatus.PROCESSING
            )
        )

        return TableReserveResponse(
            status = TableReserveStatus.PROCESSING,
        )
    }

    fun processReservation(
        request: ReservationProcessRequest,
        username: String,
        authHeader: String,
    ): ReservationProcessResponse {
        val manager = userClient.getUserByUsername(authHeader = authHeader, username = username)

        log.debug("manager extracted from token\n{}", manager.toString())

        val tableReserveTicket = findByIdOrThrow(request.tableReserveTicketId)

        log.debug("TableReserveTicket found\n{}", tableReserveTicket)

        val restaurant = restaurantClient.getRestaurantById(authHeader, tableReserveTicket.restaurantId)
            ?: throw RestaurantNotFoundException()

        log.debug("Restaurant received from restaurant-service \n{}", restaurant.toString())

        if (restaurant.managerId != manager.id) {
            throw CommonException(
                "You don't work in restaurant ${restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        if (tableReserveTicket.status != TableReserveStatus.PROCESSING) {
            throw CommonException(
                "Reservation with id ${request.tableReserveTicketId} already processed. " +
                        "Status ${tableReserveTicket.status}",
                HttpStatus.BAD_REQUEST
            )
        }

        val processedTableReserveTicket = TableReserveTicket(
            id = tableReserveTicket.id,
            restaurantId = tableReserveTicket.restaurantId,
            userId = tableReserveTicket.userId,
            creationDate = tableReserveTicket.creationDate,
            fromDate = tableReserveTicket.fromDate,
            tillDate = tableReserveTicket.tillDate,
            numOfGuests = tableReserveTicket.numOfGuests,
            userComment = tableReserveTicket.userComment,
            managerId = manager.id,
            managerComment = request.managerComment,
            status = request.status,
        )

        save(processedTableReserveTicket)

        return ReservationProcessResponse(
            id = processedTableReserveTicket.id,
            status = processedTableReserveTicket.status,
        )
    }
}