package com.ip13.main.controller

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Validated
@RestController
@RequestMapping("/reservation")
class ReserveController {
    @PostMapping("/reserve-table")
    fun reserveTable(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: TableReserveRequest
    ): TableReserveResponse {
        log.debug("/reservation/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, principal.name)
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