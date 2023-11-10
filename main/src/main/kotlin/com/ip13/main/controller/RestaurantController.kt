package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeManagerRequestDto
import com.ip13.main.model.dto.request.GradeVisitorRequestDto
import com.ip13.main.model.dto.request.RestaurantAddTicketRequestDto
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequestDto
import com.ip13.main.model.dto.response.GradeVisitorResponseDto
import com.ip13.main.model.dto.response.RestaurantAddTicketResponseDto
import com.ip13.main.model.dto.response.RestaurantProcessTicketResponseDto
import com.ip13.main.model.dto.response.ShowTicketsResponseDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.GradeManagerService
import com.ip13.main.service.GradeVisitorService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restaurant", method = [RequestMethod.POST, RequestMethod.GET])
class RestaurantController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val gradeVisitorService: GradeVisitorService,
    private val userService: UserService,
    private val tableReserveService: TableReserveService,
    private val gradeManagerService: GradeManagerService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/create_ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        restaurantAddTicketRequestDto: RestaurantAddTicketRequestDto,
    ): ResponseEntity<RestaurantAddTicketResponseDto> {
        log.debug("/restaurant/create_ticket endpoint invoked")

        val response = restaurantAddTicketService.createTicket(authHeader, restaurantAddTicketRequestDto)

        return ResponseEntity.ok(
            response,
        )
    }

    @PostMapping("/process_ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        dto: RestaurantProcessTicketRequestDto,
    ): ResponseEntity<RestaurantProcessTicketResponseDto> {
        val response = restaurantAddTicketService.processRestaurantAddTicket(authHeader, dto)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/show_tickets")
    fun showTickets(
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowTicketsResponseDto {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        log.debug("tickets found\n{}", tickets.map { it::toString })

        return ShowTicketsResponseDto(tickets)
    }

    @GetMapping("/add_grade_visitor")
    fun addGradeVisitor(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody
        gradeVisitorRequestDto: GradeVisitorRequestDto,
    ): GradeVisitorResponseDto {
        val newGrade = gradeVisitorService.gradeRestaurant(authHeader, gradeVisitorRequestDto)

        return GradeVisitorResponseDto(newGrade)
    }

    @GetMapping("/add_grade_manager")
    fun addGradeManager(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody
        gradeManagerRequestDto: GradeManagerRequestDto,
    ): ResponseEntity<String> {
        // TODO() переделать dto и порефакторить под JPA

//        // Пользователь, который отправил запрос, дальше по его id достаем менеджера
//        val user = userService.getUserByTokenInHeader(authHeader)
//
//        log.debug("user extracted from token\n{}", user.toString())
//
//        val manager = managerService.getManagerByUserIdOrNull(user.id)
//            ?: throw ManagerNotFoundException("No manager found with userId ${user.id}")
//
//        log.debug("manager loaded from db\n{}", manager.toString())
//
//        val tableReserveTicket = tableReserveService.findByIdOrNull(gradeManagerDto.tableReserveTicketId)
//            ?: throw TableReserveTicketNotFoundException(
//                "No TableReserveTicket with id ${gradeManagerDto.tableReserveTicketId}"
//            )
//
//        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())
//
//        val newGrade = gradeManagerService.gradeUser(
//            gradeManagerDto.toGradeManager(
//                managerId = manager.id,
//                userId = tableReserveTicket.userId // пользователь, которому ставим оценку
//            )
//        )

        return ResponseEntity("", HttpStatus.OK)
    }
}