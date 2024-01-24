package com.ip13.main.kafka

import com.ip13.main.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaConfig(
    private val userHandler: UserHandler,
) {
    @Bean
    fun registrationListener(): RegistrationListener {
        return RegistrationListener(userHandler)
    }
}