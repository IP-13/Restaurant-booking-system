package com.ip13.main.controller

import com.ip13.main.model.dto.request.BlackListRequest
import com.ip13.main.model.dto.request.RoleAddRequest
import com.ip13.main.model.dto.request.RoleDeleteRequest
import com.ip13.main.model.dto.response.AddRoleResponse
import com.ip13.main.model.dto.response.AddToBlackListResponse
import com.ip13.main.model.dto.response.DeleteRoleResponse
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BlackListService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/admin", method = [RequestMethod.POST, RequestMethod.GET])
class AdminController(
    val userService: UserService,
    val blackListService: BlackListService,
) {
    private val logger = getLogger(javaClass)

    @PostMapping("/add-role")
    fun addRole(
        @Valid
        @RequestBody(required = true)
        request: RoleAddRequest,
    ): AddRoleResponse {
        logger.debug("/admin/add-role endpoint invoked")

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
        logger.debug("/admin/delete-role endpoint invoked")

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
        logger.debug("/admin/add-to-black-list endpoint invoked")

        val blackListId = blackListService.processRequest(request)

        return AddToBlackListResponse(blackListId)
    }
}