package com.ip13.main.controller

import com.ip13.main.model.dto.request.AddBookingConstraintRequestDto
import com.ip13.main.model.dto.request.ReservationProcessDto
import com.ip13.main.model.dto.request.TableReserveRequestDto
import com.ip13.main.model.dto.response.AddBookingConstraintResponseDto
import com.ip13.main.model.dto.response.ShowReservationsResponseDto
import com.ip13.main.model.dto.response.TableReserveResponseDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.RestaurantService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
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
    ): TableReserveResponseDto {
        log.debug("/reserve/reserve_table endpoint invoked")

        return tableReserveService.reserveTable(tableReserveRequestDto, authHeader)
    }

    @PostMapping("/add_booking_constraint")
    fun addBookingConstraint(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        dto: AddBookingConstraintRequestDto,
    ): AddBookingConstraintResponseDto {
        log.debug("/reserve/add_booking_constraint endpoint invoked")

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
    ): ShowReservationsResponseDto {
        return tableReserveService.getReservations(authHeader, pageNumber, pageSize)
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