package com.ip13.main.webClient.blackListClient

import com.ip13.main.util.getLogger
import com.ip13.main.webClient.blackListClient.dto.BlackListResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class BlackListServiceWebClientImpl(
    private val blackListServiceClient: WebClient,
) : BlackListServiceWebClient {
    private val log = getLogger(javaClass)

    override fun getBlackListByUsername(username: String, authHeader: String): List<BlackListResponse>? {
        val blackList = blackListServiceClient
            .get()
            .uri("/black-list/get/${username}")
            .accept(MediaType.APPLICATION_NDJSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                authHeader,
            )
            .retrieve()
            .bodyToFlux(BlackListResponse::class.java)
            .collectList()
            .block()

        log.debug("black list received:\n{}", blackList?.forEach { it.toString() })

        return blackList
    }
}