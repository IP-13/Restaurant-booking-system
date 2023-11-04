package com.ip13.main.controller

import com.ip13.main.mapper.BookingConstraintMapper
import com.ip13.main.mapper.TableReserveTicketMapper
import com.ip13.main.model.dto.BookingConstraintDto
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.ManagerService
import com.ip13.main.service.RestaurantService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reserve")
class TableReserveController(
    private val tableReserveService: TableReserveService,
    private val userService: UserService,
    private val restaurantService: RestaurantService,
    private val managerService: ManagerService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

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
        dto: BookingConstraintDto,
    ): ResponseEntity<*> {
        val restaurant = restaurantService.findByIdOrNull(dto.restaurantId)
            ?: return ResponseEntity("No restaurant found with id ${dto.restaurantId}", HttpStatus.BAD_REQUEST)

        log.debug("Restaurant found\n{}", restaurant)

        val isWorkingInRestaurant = managerService.checkIfWorksInRestaurantById(dto.managerId, dto.restaurantId)

        if (!isWorkingInRestaurant) {
            return ResponseEntity(
                "Manager with id ${dto.managerId} does not work in restaurant with id ${dto.restaurantId}",
                HttpStatus.BAD_REQUEST
            )
        }

        val bookingConstraint = BookingConstraintMapper.fromBookingConstraintDto(dto)

        val bookingConstraintId = bookingConstraintService.save(bookingConstraint)

        return ResponseEntity("Booking constraint successfully added with id $bookingConstraintId", HttpStatus.OK)
    }
}