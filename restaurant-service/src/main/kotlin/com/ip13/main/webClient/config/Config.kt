package com.ip13.main.webClient.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Config {
    @LoadBalanced
    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }
}