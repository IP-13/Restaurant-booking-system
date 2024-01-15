package com.ip13.main.webClient.blackListClient

import com.ip13.main.webClient.blackListClient.dto.BlackListResponse

fun interface BlackListServiceWebClient {
    fun getBlackListByUsername(username: String, authHeader: String): List<BlackListResponse>?
}