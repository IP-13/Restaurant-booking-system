package com.ip13.main.kafka

import com.ip13.main.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListenerConfig(
    private val userService: UserService,
) {
    @Bean
    fun restaurantCreatedListener(): RestaurantCreatedListener {
        return RestaurantCreatedListener(userService)
    }
}
