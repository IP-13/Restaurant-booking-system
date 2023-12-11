package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
import com.ip13.main.webClient.blackListClient.BlackListServiceWebClient
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.ReservationProcessResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.model.dto.response.TableReserveTicketResponse
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.model.toTableReserveTicket
import com.ip13.main.model.toTableReserveTicketResponse
import com.ip13.main.repository.TableReserveTicketRepository
import com.ip13.main.util.getLogger
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TableReserveService(
    private val tableReserveTicketRepository: TableReserveTicketRepository,
    private val restaurantService: RestaurantService,
    private val blackListServiceWebClient: BlackListServiceWebClient,
) {
    private val log = getLogger(javaClass)

    fun save(tableReserveTicket: TableReserveTicket): TableReserveTicket {
        return tableReserveTicketRepository.save(tableReserveTicket)
    }

    fun findByIdOrNull(id: Int): TableReserveTicketResponse? {
        return tableReserveTicketRepository.findByIdOrNull(id)?.toTableReserveTicketResponse()
    }

    /**
     * @throws TableReserveTicketNotFoundException if TableReserveTicket with that id doesn't exist
     */
    fun findByIdOrThrow(id: Int): TableReserveTicket {
        return tableReserveTicketRepository.findByIdOrNull(id)
            ?: throw TableReserveTicketNotFoundException("No table reserve ticket with id $id")
    }

    fun reserveTable(
        request: TableReserveRequest,
        username: String,
        authHeader: String,
    ): TableReserveResponse {
        val restaurant = restaurantService.findByIdOrThrow(request.restaurantId)

        val blackListEntries =
            blackListServiceWebClient.getBlackListByUsername(authHeader = authHeader, username = username)

        if (blackListEntries?.isNotEmpty() != false) {
            val managerComment = "You're in a black list for bad behaviour"

            save(
                request.toTableReserveTicket(
                    restaurant = restaurant,
                    username = username,
                    managerComment = managerComment,
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponse(
                status = TableReserveStatus.REJECTED,
                managerComment = managerComment,
            )
        }

        val bookingConstraints = restaurant.bookingConstraints.filter {
            request.fromDate < it.tillDate && request.fromDate >= it.fromDate ||
                    request.tillDate <= it.tillDate && request.tillDate > it.fromDate
        }

        if (bookingConstraints.isNotEmpty()) {
            val managerComment = "Sorry, restaurant ${restaurant.id} is closed at that time"

            save(
                request.toTableReserveTicket(
                    restaurant = restaurant,
                    username = username,
                    managerComment = managerComment,
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponse(
                status = TableReserveStatus.REJECTED,
                managerComment = managerComment,
            )
        }

        save(
            request.toTableReserveTicket(
                restaurant = restaurant,
                username = username,
                managerComment = null,
                status = TableReserveStatus.PROCESSING
            )
        )

        return TableReserveResponse(
            status = TableReserveStatus.PROCESSING,
            managerComment = null,
        )
    }

    fun processReservation(
        request: ReservationProcessRequest,
        managerName: String,
    ): ReservationProcessResponse {
        val tableReserveTicket = findByIdOrThrow(request.tableReserveTicketId)

        log.debug("TableReserveTicket found\n{}", tableReserveTicket)

        val restaurant = restaurantService.findByIdOrThrow(tableReserveTicket.restaurant.id)

        log.debug("Restaurant received from restaurant-service \n{}", restaurant.toString())

        if (restaurant.managerName != managerName) {
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
            restaurant = tableReserveTicket.restaurant,
            username = tableReserveTicket.username,
            creationDate = tableReserveTicket.creationDate,
            fromDate = tableReserveTicket.fromDate,
            tillDate = tableReserveTicket.tillDate,
            numOfGuests = tableReserveTicket.numOfGuests,
            userComment = tableReserveTicket.userComment,
            managerName = managerName,
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