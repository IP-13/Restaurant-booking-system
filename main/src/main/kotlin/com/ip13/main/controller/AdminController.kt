package com.ip13.main.controller

import com.ip13.main.model.dto.request.BlackListRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.request.RoleAddRequest
import com.ip13.main.model.dto.request.RoleDeleteRequest
import com.ip13.main.model.dto.response.*
import com.ip13.main.model.toRestaurantAddTicketResponse
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BlackListService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/admin", method = [RequestMethod.POST, RequestMethod.GET])
class AdminController(
    val userService: UserService,
    val blackListService: BlackListService,
    val restaurantAddTicketService: RestaurantAddTicketService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/add-role")
    fun addRole(
        @Valid
        @RequestBody(required = true)
        request: RoleAddRequest,
    ): AddRoleResponse {
        log.debug("/admin/add-role endpoint invoked")

        val isAdded = userService.addRole(request)

        return if (isAdded) {
            AddRoleResponse("Role ${request.role} successfully added to user ${request.userId}")
        } else {
            AddRoleResponse("User ${request.userId} already has role ${request.role}")
        }
    }

    @PostMapping("/delete-role")
    fun deleteRole(
        @Valid
        @RequestBody(required = true)
        request: RoleDeleteRequest,
    ): DeleteRoleResponse {
        log.debug("/admin/delete-role endpoint invoked")

        val isDeleted = userService.deleteRole(request)

        return if (isDeleted) {
            DeleteRoleResponse("Role ${request.role} successfully deleted from user ${request.userId}")
        } else {
            DeleteRoleResponse("User ${request.userId} does not have role ${request.role} to delete")
        }
    }

    @PostMapping("/add-to-black-list")
    fun addToBlackList(
        @Valid
        @RequestBody(required = true)
        request: BlackListRequest
    ): AddToBlackListResponse {
        log.debug("/admin/add-to-black-list endpoint invoked")

        val blackListId = blackListService.processRequest(request)

        return AddToBlackListResponse(blackListId)
    }

    @PostMapping("/process-ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantProcessTicketRequest,
    ): RestaurantProcessTicketResponse {
        log.debug("/restaurant/process-ticket endpoint invoked")

        return restaurantAddTicketService.processRestaurantAddTicket(authHeader, request)
    }

    @GetMapping("/show-tickets")
    fun showTickets(
        @PositiveOrZero
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @PositiveOrZero
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowTicketsResponse {
        log.debug("/restaurant/show-tickets endpoint invoked")

        val tickets = restaurantAddTicketService.getTickets(pageNumber, pageSize)

        return ShowTicketsResponse(tickets.map { it.toRestaurantAddTicketResponse() })
    }
}