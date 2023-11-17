package com.ip13.main.controller

import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.AddBookingConstraintResponse
import com.ip13.main.model.dto.response.ReservationProcessResponse
import com.ip13.main.model.dto.response.ShowReservationsResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/reserve", method = [RequestMethod.POST, RequestMethod.GET])
class ReserveController(
    private val tableReserveService: TableReserveService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/reserve-table")
    fun reserveTable(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: TableReserveRequest
    ): TableReserveResponse {
        log.debug("/reserve/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, authHeader)
    }

    @PostMapping("/add-booking-constraint")
    fun addBookingConstraint(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: AddBookingConstraintRequest,
    ): AddBookingConstraintResponse {
        log.debug("/reserve/add-booking-constraint endpoint invoked")

        return bookingConstraintService.addBookingConstraint(authHeader, request)
    }

    @GetMapping("/show-reservations")
    fun showRestaurant(
        @PositiveOrZero
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @PositiveOrZero
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
    ): ShowReservationsResponse {
        log.debug("/reserve/show-reservations endpoint invoked")

        return tableReserveService.getReservations(authHeader, pageNumber, pageSize)
    }

    @PostMapping("/process-reservation")
    fun processReservation(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: ReservationProcessRequest,
    ): ReservationProcessResponse {
        log.debug("/reserve/process-reservation endpoint invoked")

        return tableReserveService.processReservation(authHeader, request)
    }
}