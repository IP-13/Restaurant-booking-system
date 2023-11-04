package com.ip13.main.controller

import com.ip13.main.mapper.TableReserveTicketMapper
import com.ip13.main.model.dto.BookingConstraintDto
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.TableReserveService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reserve")
class TableReserveController(
    val tableReserveService: TableReserveService,
    val userService: UserService,
) {
    @PostMapping("reserve_table")
    fun reserveTable(
        @RequestBody
        tableReserveTicketDto: TableReserveTicketDto
    ): ResponseEntity<String> {
        val userId = tableReserveTicketDto.userId

        userService.checkUser(userId)

        val expirationDateFromBlackList = userService.getExpirationDateFromBlackList(userId)

        if (expirationDateFromBlackList != null) {
            return ResponseEntity(
                "You're toxic person and you cannot use our service for quite some time. " +
                        "Exactly till $expirationDateFromBlackList",
                HttpStatus.BAD_REQUEST
            )
        }

        val createdTicketId = tableReserveService.save(
            TableReserveTicketMapper.fromTableReserveTicketDto(
                tableReserveTicketDto
            )
        )

        return ResponseEntity(
            "Reserve ticket has been successfully created with id: $createdTicketId",
            HttpStatus.OK
        )
    }

    @PostMapping("/add_booking_constraint")
    fun addBookingConstraint(
        @RequestBody
        bookingConstraintDto: BookingConstraintDto,
    ) {

    }
}