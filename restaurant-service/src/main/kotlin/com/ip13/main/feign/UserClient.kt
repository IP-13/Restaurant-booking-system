package com.ip13.main.feign

import com.ip13.main.feign.dto.AddRoleResponse
import com.ip13.main.feign.dto.RoleAddRequest
import com.ip13.main.model.entity.User
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "user-service")
interface UserClient {
    @GetMapping("/auth/user/username")
    fun getUserByUsername(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        @RequestParam
        username: String
    ): User

    @PostMapping("auth/role")
    fun addRole(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        @RequestBody
        roleAddRequest: RoleAddRequest
    ): AddRoleResponse
}