package com.ip13.main.controller

import com.ip13.main.model.dto.request.BlackListDto
import com.ip13.main.model.dto.request.RoleAddDto
import com.ip13.main.model.dto.request.RoleDeleteDto
import com.ip13.main.model.toBlackList
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
        roleAddDto: RoleAddDto,
    ): ResponseEntity<String> {
        logger.debug("/admin/add_role endpoint invoked")

        val isAdded = userService.addRole(roleAddDto)

        return if (isAdded) {
            ResponseEntity.ok("Role ${roleAddDto.role} successfully added to user ${roleAddDto.userId}")
        } else {
            ResponseEntity.ok("User ${roleAddDto.userId} already has role ${roleAddDto.role}")
        }
    }

    @GetMapping("/get_authentication")
    fun getAuthentication(): ResponseEntity<Authentication> {
        return ResponseEntity.ok(SecurityContextHolder.getContext().authentication)
    }

    @PostMapping("/delete_role")
    fun deleteRole(
        @RequestBody(required = true)
        roleDeleteDto: RoleDeleteDto,
    ): ResponseEntity<String> {
        logger.debug("/admin/delete_role endpoint invoked")

        val isDeleted = userService.deleteRole(roleDeleteDto)

        return if (isDeleted) {
            ResponseEntity.ok("Role ${roleDeleteDto.role} successfully deleted from user ${roleDeleteDto.userId}")
        } else {
            ResponseEntity.ok("User ${roleDeleteDto.userId} does not have role ${roleDeleteDto.role} to delete")
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
        blackListDto: BlackListDto
    ): ResponseEntity<Int> {
        logger.debug("/admin/add_to_black_list endpoint invoked")

        val blackListId = blackListService.save(blackListDto.toBlackList())

        return ResponseEntity.ok(blackListId)
    }
}