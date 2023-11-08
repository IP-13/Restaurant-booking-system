package com.ip13.main.controller

import com.ip13.main.model.dto.RoleAddDto
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(
    val userService: UserService,
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
}