package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
import com.ip13.main.model.dto.request.ReservationProcessRequestDto
import com.ip13.main.model.dto.request.TableReserveRequestDto
import com.ip13.main.model.dto.response.ReservationProcessResponseDto
import com.ip13.main.model.dto.response.ShowReservationsResponseDto
import com.ip13.main.model.dto.response.TableReserveResponseDto
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.model.toTableReserveTicket
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

    fun getReservations(authHeader: String, pageNumber: Int, pageSize: Int): ShowReservationsResponseDto {
        val manager = userService.getUserByTokenInHeader(authHeader)

        log.debug("manager extracted from token\n{}", manager.toString())

        val restaurant = restaurantService.findByManagerId(manager.id)

        log.debug("restaurant found by manager id\n{}", restaurant.toString())

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val reservations =
            tableReserveTicketRepository.findAll(pageRequest).filter { it.restaurant.id == restaurant.id }.toList()

        log.debug("reservations found\n{}", reservations.map { it.toString() })

        return ShowReservationsResponseDto(reservations)
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

    fun reserveTable(dto: TableReserveRequestDto, authHeader: String): TableReserveResponseDto {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val restaurant = restaurantService.findByIdOrThrow(dto.restaurantId)

        if (user.blackListEntries.isNotEmpty()) {
            save(
                dto.toTableReserveTicket(
                    restaurant = restaurant,
                    user = user,
                    managerComment = "You're in a black list for bad behaviour",
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponseDto(
                blackListEntries = user.blackListEntries,
                bookingConstraints = listOf(),
                status = TableReserveStatus.REJECTED,
                comment = "You're in a black list for bad behaviour",
            )

        }

        val bookingConstraints = restaurant.bookingConstraints.filter {
            dto.fromDate < it.tillDate && dto.fromDate >= it.fromDate ||
                    dto.tillDate <= it.tillDate && dto.tillDate > it.fromDate
        }

        if (bookingConstraints.isNotEmpty()) {
            save(
                dto.toTableReserveTicket(
                    restaurant = restaurant,
                    user = user,
                    managerComment = "Sorry, restaurant ${restaurant.id} is closed at that time",
                    status = TableReserveStatus.REJECTED
                )
            )

            return TableReserveResponseDto(
                blackListEntries = user.blackListEntries,
                bookingConstraints = bookingConstraints,
                status = TableReserveStatus.REJECTED,
                comment = "Sorry, restaurant ${restaurant.id} is closed at that time",
            )
        }

        save(
            dto.toTableReserveTicket(
                restaurant = restaurant,
                user = user,
                managerComment = null,
                status = TableReserveStatus.PROCESSING
            )
        )

        return TableReserveResponseDto(
            blackListEntries = user.blackListEntries,
            bookingConstraints = bookingConstraints,
            status = TableReserveStatus.PROCESSING,
            comment = "Your ticket successfully added"
        )
    }

    fun processReservation(authHeader: String, dto: ReservationProcessRequestDto): ReservationProcessResponseDto {
        val manager = userService.getUserByTokenInHeader(authHeader)

        log.debug("manager extracted from token\n{}", manager.toString())

        val tableReserveTicket = findByIdOrThrow(dto.tableReserveTicketId)

        log.debug("TableReserveTicket found\n{}", tableReserveTicket)

        if (tableReserveTicket.restaurant.manager.id != manager.id) {
            throw CommonException(
                "You don't work in restaurant ${tableReserveTicket.restaurant.id}",
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
            managerComment = dto.managerComment,
            status = dto.status,
        )

        return ReservationProcessResponseDto(
            id = processedTableReserveTicket.id,
            status = processedTableReserveTicket.status,
        )
    }
}