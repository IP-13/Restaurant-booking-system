package com.ip13.main.feign.userClient

import com.ip13.main.feign.userClient.dto.RoleAddRequest
import com.ip13.main.feign.userClient.dto.User
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "user-service")
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
    ): String
}