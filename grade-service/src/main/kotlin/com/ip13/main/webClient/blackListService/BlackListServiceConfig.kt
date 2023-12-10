package com.ip13.main.webClient.blackListService

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Component
class BlackListServiceConfig {
    @Autowired
    private lateinit var reactorLoadBalancerExchangeFilterFunction: ReactorLoadBalancerExchangeFilterFunction

    @Bean
    @Qualifier("blackListServiceClient")
    fun blackListServiceClient(): WebClient {
        val httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                it.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }

        return WebClient.builder()
            .filter(reactorLoadBalancerExchangeFilterFunction)
            .baseUrl("http://black-list-service")
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}