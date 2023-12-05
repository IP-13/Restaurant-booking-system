package com.ip13.main.feign.restaurantClient

import com.ip13.main.feign.restaurantClient.dto.Restaurant
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "restaurant-service")
interface RestaurantClient {
    @GetMapping("/restaurant/id/{id}")
    fun getRestaurantById(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        @PathVariable
        id: Int
    ): Restaurant?

    @GetMapping("/restaurant/grade/{id}")
    fun addGrade(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        @PathVariable
        id: Int,
        @RequestParam
        grade: Int,
    ): Int
}