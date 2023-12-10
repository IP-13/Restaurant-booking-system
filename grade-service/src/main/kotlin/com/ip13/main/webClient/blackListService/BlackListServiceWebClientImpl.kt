package com.ip13.main.webClient.blackListService

import com.ip13.main.webClient.blackListService.dto.BlackListRequest
import com.ip13.main.webClient.blackListService.dto.BlackListResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class BlackListServiceWebClientImpl(
    private val blackListServiceClient: WebClient,
) : BlackListServiceWebClient {
    override fun addToBlackList(blackListRequest: BlackListRequest, authHeader: String): Mono<BlackListResponse> =
        blackListServiceClient
            .post()
            .uri("/black-list/add")
            .accept(MediaType.APPLICATION_JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                authHeader,
            )
            .bodyValue(blackListRequest)
            .retrieve()
            .bodyToMono(BlackListResponse::class.java)
}