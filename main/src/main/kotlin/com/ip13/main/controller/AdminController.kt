package com.ip13.main.controller

import com.ip13.main.model.dto.RoleAddDto
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.security.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(
    val userService: UserService,
) {
    @PostMapping("/add_role")
    fun addRole(
        @RequestBody(required = true)
        roleAddDto: RoleAddDto,
    ): ResponseEntity<*> {
        // TODO() параметр принимается строкой, преобразуется в enum, проверяется, преобразуется обратно в строку
        val role = try {
            Role.valueOf(roleAddDto.role)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity("No such role: ${roleAddDto.role}", HttpStatus.BAD_REQUEST)
        }

        // TODO() is it ok?
        userService.checkUser(roleAddDto.userId)

        val isAlreadyHasRole = userService.checkRole(roleAddDto.userId, role.code)

        return if (isAlreadyHasRole) {
            ResponseEntity(
                "User with id: ${roleAddDto.userId} already has role: ${role.code}",
                HttpStatus.OK
            )
        } else {
            val isRoleAdded = userService.addRole(roleAddDto.userId, role.code)
            if (isRoleAdded) {
                ResponseEntity(
                    "Role: ${role.code} successfully added to user with id: ${roleAddDto.userId}",
                    HttpStatus.OK
                )
            } else {
                ResponseEntity(
                    "Something went wrong and role: ${role.code} had not been added to user with id:" +
                            " ${roleAddDto.userId}",
                    HttpStatus.BAD_REQUEST
                )
            }
        }
    }

    @GetMapping("/get_authentication")
    fun getRoles(): ResponseEntity<*> {
        return ResponseEntity(SecurityContextHolder.getContext().authentication, HttpStatus.OK)
    }

}