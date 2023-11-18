package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
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
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TableReserveService(
    private val tableReserveTicketRepository: TableReserveTicketRepository,
    private val userService: UserService,
    private val restaurantService: RestaurantService,
) {
    private val log = getLogger(javaClass)

    fun save(tableReserveTicket: TableReserveTicket): TableReserveTicket {
        return tableReserveTicketRepository.save(tableReserveTicket)
    }

    fun getReservations(authHeader: String, pageNumber: Int, pageSize: Int): ShowReservationsResponse {
        val manager = userService.getUserByTokenInHeader(authHeader)

        log.debug("manager extracted from token\n{}", manager.toString())

        val restaurant = restaurantService.findByManagerId(manager.id)

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

    fun reserveTable(request: TableReserveRequest, authHeader: String): TableReserveResponse {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val restaurant = restaurantService.findByIdOrThrow(request.restaurantId)

        if (user.blackListEntries.isNotEmpty()) {
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

    fun processReservation(authHeader: String, request: ReservationProcessRequest): ReservationProcessResponse {
        val manager = userService.getUserByTokenInHeader(authHeader)

        log.debug("manager extracted from token\n{}", manager.toString())

        val tableReserveTicket = findByIdOrThrow(request.tableReserveTicketId)

        log.debug("TableReserveTicket found\n{}", tableReserveTicket)

        if (tableReserveTicket.restaurant.manager.id != manager.id) {
            throw CommonException(
                "You don't work in restaurant ${tableReserveTicket.restaurant.id}",
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
            restaurant = tableReserveTicket.restaurant,
            user = tableReserveTicket.user,
            creationDate = tableReserveTicket.creationDate,
            fromDate = tableReserveTicket.fromDate,
            tillDate = tableReserveTicket.tillDate,
            numOfGuests = tableReserveTicket.numOfGuests,
            userComment = tableReserveTicket.userComment,
            manager = manager,
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