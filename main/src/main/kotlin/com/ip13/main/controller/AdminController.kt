package com.ip13.main.controller

import com.ip13.main.model.dto.RoleAddDto
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<*> {
        logger.debug("/admin/add_role endpoint invoked")

        // TODO() параметр принимается строкой, преобразуется в enum, проверяется, преобразуется обратно в строку
        val role = try {
            Role.valueOf(roleAddDto.role)
        } catch (ex: IllegalArgumentException) {
            logger.debug("Role \"${roleAddDto.role}\" not found")
            return ResponseEntity("No such role \"${roleAddDto.role}\"", HttpStatus.BAD_REQUEST)
        }

        // TODO() is it ok?
        userService.checkUser(roleAddDto.userId)

        // TODO() сначала чекаем юзера, потом роли этого юзера. Первая проверка юзера выглядит необязательной,
        // но без нее не получается отдать пользователю правильный ответ
        val isAlreadyHasRole = userService.checkRole(roleAddDto.userId, role.name)

        return if (isAlreadyHasRole) {
            ResponseEntity(
                "User with id: ${roleAddDto.userId} already has role: ${role.name}",
                HttpStatus.OK
            )
        } else {
            val isRoleAdded = userService.addRole(roleAddDto.userId, role.name)
            if (isRoleAdded) {
                ResponseEntity(
                    "Role: ${role.name} successfully added to user with id: ${roleAddDto.userId}",
                    HttpStatus.OK
                )
            } else {
                ResponseEntity(
                    "Something went wrong and role: ${role.name} had not been added to user with id:" +
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