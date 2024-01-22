package com.ip13.main.feign.userClient

import com.ip13.main.feign.userClient.dto.RoleAddRequest
import com.ip13.main.feign.userClient.dto.User
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@Retry(name = "user-service-retry")
@CircuitBreaker(name = "user-service-breaker")
@FeignClient(name = "user-service-service")
interface UserClient {
    @GetMapping("/auth/user/{username}")
    fun getUserByUsername(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        @PathVariable
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