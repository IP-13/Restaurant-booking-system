package com.ip13.main.webClient.blackListService

import com.ip13.main.util.getLogger
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
    private val log = getLogger(javaClass)

    override fun addToBlackList(blackListRequest: BlackListRequest, authHeader: String): Mono<BlackListResponse> {
        log.debug("sending request to black list service")

        return blackListServiceClient
            .post()
            .uri("/black-list/add")
            .accept(MediaType.APPLICATION_NDJSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                authHeader,
            )
            .bodyValue(blackListRequest)
            .retrieve()
            .bodyToMono(BlackListResponse::class.java)
    }
}