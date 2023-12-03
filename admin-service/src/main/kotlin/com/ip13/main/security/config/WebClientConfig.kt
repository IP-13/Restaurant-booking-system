package com.ip13.main.security.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {
    @Autowired
    private lateinit var reactorLoadBalancerExchangeFilterFunction: ReactorLoadBalancerExchangeFilterFunction

    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                it.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }

        return WebClient.builder().filter(reactorLoadBalancerExchangeFilterFunction)
            .baseUrl("http://authorization-service/auth")
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}