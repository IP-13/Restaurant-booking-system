package com.ip13.main.controller

import com.ip13.main.model.dto.request.BlackListRequestDto
import com.ip13.main.model.dto.request.RoleAddRequestDto
import com.ip13.main.model.dto.request.RoleDeleteRequestDto
import com.ip13.main.security.service.UserService
import com.ip13.main.service.BlackListService
import com.ip13.main.util.getLogger
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
        @RequestBody(required = true)
        roleAddRequestDto: RoleAddRequestDto,
    ): ResponseEntity<String> {
        logger.debug("/admin/add_role endpoint invoked")

        val isAdded = userService.addRole(roleAddRequestDto)

        return if (isAdded) {
            ResponseEntity.ok("Role ${roleAddRequestDto.role} successfully added to user ${roleAddRequestDto.userId}")
        } else {
            ResponseEntity.ok("User ${roleAddRequestDto.userId} already has role ${roleAddRequestDto.role}")
        }
    }

    @GetMapping("/get_authentication")
    fun getAuthentication(): ResponseEntity<Authentication> {
        return ResponseEntity.ok(SecurityContextHolder.getContext().authentication)
    }

    @PostMapping("/delete_role")
    fun deleteRole(
        @RequestBody(required = true)
        roleDeleteRequestDto: RoleDeleteRequestDto,
    ): ResponseEntity<String> {
        logger.debug("/admin/delete_role endpoint invoked")

        val isDeleted = userService.deleteRole(roleDeleteRequestDto)

        return if (isDeleted) {
            ResponseEntity.ok("Role ${roleDeleteRequestDto.role} successfully deleted from user ${roleDeleteRequestDto.userId}")
        } else {
            ResponseEntity.ok("User ${roleDeleteRequestDto.userId} does not have role ${roleDeleteRequestDto.role} to delete")
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
        @RequestBody(required = true)
        blackListRequestDto: BlackListRequestDto
    ): ResponseEntity<Int> {
        logger.debug("/admin/add_to_black_list endpoint invoked")

        val blackListId = blackListService.processRequest(blackListRequestDto)

        return ResponseEntity.ok(blackListId)
    }
}