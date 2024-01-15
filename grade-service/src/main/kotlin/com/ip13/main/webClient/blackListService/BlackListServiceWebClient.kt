package com.ip13.main.webClient.blackListService

import com.ip13.main.webClient.blackListService.dto.BlackListRequest
import com.ip13.main.webClient.blackListService.dto.BlackListResponse
import reactor.core.publisher.Mono

fun interface BlackListServiceWebClient {
    fun addToBlackList(blackListRequest: BlackListRequest, authHeader: String): Mono<BlackListResponse>
}