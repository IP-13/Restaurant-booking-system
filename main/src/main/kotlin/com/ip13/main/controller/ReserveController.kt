package com.ip13.main.controller

import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.model.dto.BookingConstraintDto
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.model.toBookingConstraint
import com.ip13.main.model.toTableReserveTicket
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.ManagerService
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
    private val managerService: ManagerService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("reserve_table")
    fun reserveTable(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        tableReserveTicketDto: TableReserveTicketDto
    ): ResponseEntity<String> {
        val user = userService.getUserByTokenInHeader(authHeader)

        // TODO() пользователя уже проверяли в филтрах, так что отсюда можно удалить
        userService.checkUser(user.id)

        val expirationDateFromBlackList = userService.getExpirationDateFromBlackList(user.id)

        if (expirationDateFromBlackList != null) {
            return ResponseEntity(
                "You're toxic person and you cannot use our service for quite some time. " +
                        "Exactly till $expirationDateFromBlackList",
                HttpStatus.BAD_REQUEST
            )
        }

        val createdTicketId = tableReserveService.save(
            tableReserveTicketDto.toTableReserveTicket(user.id)
        )

        return ResponseEntity(
            "Reserve ticket has been successfully created with id: $createdTicketId",
            HttpStatus.OK
        )
    }

    @PostMapping("/add_booking_constraint")
    fun addBookingConstraint(
        // Проверка токена уже была на стадии фильтров, так что если дошли до этого места, то хедер должен быть точно
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        dto: BookingConstraintDto,
    ): ResponseEntity<*> {
        val restaurant = restaurantService.findByIdOrNull(dto.restaurantId)
            ?: throw RestaurantNotFoundException("No restaurant with id ${dto.restaurantId}")

        log.debug("Restaurant found\n{}", restaurant.toString())

        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val manager = managerService.getManagerByUserId(user.id)

        log.debug("manager loaded from db\n{}", manager.toString())

        val managerId = manager.id
        val restaurantId = restaurant.id

        val isWorkingInRestaurant = managerService.checkIfWorksInRestaurantById(managerId, restaurantId)

        if (!isWorkingInRestaurant) {
            return ResponseEntity(
                "Manager with id $managerId does not work in restaurant with id $restaurantId",
                HttpStatus.BAD_REQUEST
            )
        }

        val bookingConstraint = dto.toBookingConstraint(managerId)

        val bookingConstraintId = bookingConstraintService.save(bookingConstraint)

        return ResponseEntity("Booking constraint successfully added with id $bookingConstraintId", HttpStatus.OK)
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

        val manager = managerService.getManagerByUserId(user.id)

        log.debug("manager loaded from db\n{}", manager.toString())

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val reservations = tableReserveService.getReservations(pageRequest)

        log.debug("tickets found\n{}", reservations.map { it::toString })

        return ResponseEntity(reservations, HttpStatus.OK)
    }
}