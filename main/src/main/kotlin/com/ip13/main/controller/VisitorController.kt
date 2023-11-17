package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.GradeRestaurantResponse
import com.ip13.main.model.dto.response.RestaurantCreateTicketResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.service.GradeVisitorService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/visitor", method = [RequestMethod.POST, RequestMethod.GET])
class VisitorController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val gradeVisitorService: GradeVisitorService,
    private val tableReserveService: TableReserveService,
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
        log.debug("/visitor/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, authHeader)
    }

    @PostMapping("/create-ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantAddTicketRequest,
    ): RestaurantCreateTicketResponse {
        log.debug("/visitor/create-ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(authHeader, request)
    }

    @GetMapping("/grade-restaurant")
    fun gradeRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: GradeRestaurantRequest,
    ): GradeRestaurantResponse {
        log.debug("/visitor/grade-restaurant endpoint invoked")

        val newGrade = gradeVisitorService.gradeRestaurant(authHeader, request)

        return GradeRestaurantResponse(newGrade)
    }
}