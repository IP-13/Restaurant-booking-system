package com.ip13.main.webClient.restaurantService

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class Config {
    @Autowired
    private lateinit var reactorLoadBalancerExchangeFilterFunction: ReactorLoadBalancerExchangeFilterFunction

    @Bean
    @Qualifier("restaurantServiceClient")
    fun restaurantServiceClient(): WebClient {
        val httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                it.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }

        return WebClient.builder()
            .filter(reactorLoadBalancerExchangeFilterFunction)
            .baseUrl("http://restaurant-service")
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}
