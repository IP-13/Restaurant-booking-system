package com.ip13.main.controller

import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.AddBookingConstraintResponse
import com.ip13.main.model.dto.response.ReservationProcessResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Validated
@RestController
@RequestMapping("/reservation")
class ReserveController(
    private val tableReserveService: TableReserveService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/reserve-table")
    fun reserveTable(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: TableReserveRequest
    ): TableReserveResponse {
        log.debug("/reservation/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, principal.name, authHeader)
    }

    @PostMapping("/process-reservation")
    fun processReservation(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: ReservationProcessRequest,
    ): ReservationProcessResponse {
        log.debug("/reservation/process-reservation endpoint invoked")

        return tableReserveService.processReservation(request, principal.name, authHeader)
    }

    @PostMapping("/add-booking-constraint")
    fun addBookingConstraint(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: AddBookingConstraintRequest,
    ): AddBookingConstraintResponse {
        log.debug("/reservation/add-booking-constraint endpoint invoked")

        return bookingConstraintService.addBookingConstraint(request, principal.name, authHeader)
    }
}