package com.ip13.main.webClient.blackListService.dto

import java.time.LocalDateTime

data class BlackListResponse(
    val id: Int,
    val username: String,
    val fromDate: LocalDateTime,
    val tillDate: LocalDateTime,
    val reason: String?,
)