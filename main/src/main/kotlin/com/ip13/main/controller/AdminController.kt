package com.ip13.main.controller

import com.ip13.main.model.dto.request.BlackListRequest
import com.ip13.main.model.dto.request.RoleAddRequest
import com.ip13.main.model.dto.request.RoleDeleteRequest
import com.ip13.main.model.dto.response.AddRoleResponse
import com.ip13.main.model.dto.response.AddToBlackListResponse
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BlackListService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin", method = [RequestMethod.POST, RequestMethod.GET])
class AdminController(
    val userService: UserService,
    val blackListService: BlackListService,
) {
    private val logger = getLogger(javaClass)

    @PostMapping("/add_role")
    fun addRole(
        @Valid
        @RequestBody(required = true)
        request: RoleAddRequest,
    ): AddRoleResponse {
        logger.debug("/admin/add_role endpoint invoked")

        val isAdded = userService.addRole(request)

        return if (isAdded) {
            AddRoleResponse("Role ${request.role} successfully added to user ${request.userId}")
        } else {
            AddRoleResponse("User ${request.userId} already has role ${request.role}")
        }
    }

    @GetMapping("/get_authentication")
    fun getAuthentication(): ResponseEntity<Authentication> {
        return ResponseEntity.ok(SecurityContextHolder.getContext().authentication)
    }

    @PostMapping("/delete_role")
    fun deleteRole(
        @Valid
        @RequestBody(required = true)
        request: RoleDeleteRequest,
    ): ResponseEntity<String> {
        logger.debug("/admin/delete_role endpoint invoked")

        val isDeleted = userService.deleteRole(request)

        return if (isDeleted) {
            ResponseEntity.ok("Role ${request.role} successfully deleted from user ${request.userId}")
        } else {
            ResponseEntity.ok("User ${request.userId} does not have role ${request.role} to delete")
        }
    }

    @PostMapping("/delete_user/{userId}")
    fun deleteUser(
        @PathVariable(required = true)
        userId: Int
    ): ResponseEntity<String> {
        logger.debug("/admin/delete_user endpoint invoked")

        userService.deleteUser(userId)

        return ResponseEntity.ok("User with id $userId was deleted")
    }

    @PostMapping("/add_to_black_list")
    fun addToBlackList(
        @Valid
        @RequestBody(required = true)
        request: BlackListRequest
    ): AddToBlackListResponse {
        logger.debug("/admin/add_to_black_list endpoint invoked")

        val blackListId = blackListService.processRequest(request)

        return AddToBlackListResponse(blackListId)
    }
}