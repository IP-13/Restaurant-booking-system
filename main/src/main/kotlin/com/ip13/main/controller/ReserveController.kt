package com.ip13.main.controller

import com.ip13.main.model.dto.request.AddBookingConstraintRequestDto
import com.ip13.main.model.dto.request.ReservationProcessDto
import com.ip13.main.model.dto.request.TableReserveRequestDto
import com.ip13.main.model.dto.response.AddBookingConstraintResponseDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.RestaurantService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reserve", method = [RequestMethod.POST, RequestMethod.GET])
class ReserveController(
    private val tableReserveService: TableReserveService,
    private val userService: UserService,
    private val restaurantService: RestaurantService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/reserve_table")
    fun reserveTable(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        tableReserveRequestDto: TableReserveRequestDto
    ): ResponseEntity<String> {
        log.debug("/reserve/reserve_table endpoint invoked")

        tableReserveService.reserveTable(tableReserveRequestDto, authHeader)

        return ResponseEntity("", HttpStatus.OK)
    }

    @PostMapping("/add_booking_constraint")
    fun addBookingConstraint(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        dto: AddBookingConstraintRequestDto,
    ): AddBookingConstraintResponseDto {
        return bookingConstraintService.addBookingConstraint(authHeader, dto)
    }

    @GetMapping("/show_reservations")
    fun showRestaurant(
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @RequestHeader(name = "page_size")
        pageSize: Int,
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
    ): ResponseEntity<*> {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        // TODO() проверить работает ли в этом ресторане

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val reservations = tableReserveService.getReservations(pageRequest)

        log.debug("tickets found\n{}", reservations.map { it::toString })

        return ResponseEntity(reservations, HttpStatus.OK)
    }

    @PostMapping("/process_reservation")
    fun processReservation(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody
        reservationProcessDto: ReservationProcessDto,
    ): ResponseEntity<*> {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        // TODO() проверить работает ли в этом ресторане

        // TODO() переделать dto и порефакторить под JPA

//        val updatedCount = tableReserveService.processReservation(reservationProcessDto, manager.id)
//
//        return if (updatedCount == 1) {
//            ResponseEntity("Successfully updated", HttpStatus.OK)
//        } else {
//            ResponseEntity(
//                "Something went wrong. Reservation ${reservationProcessDto.tableReserveTicketId} was not updated",
//                HttpStatus.INTERNAL_SERVER_ERROR
//            )
//        }

        return ResponseEntity("", HttpStatus.OK)
    }
}