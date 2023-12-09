package com.ip13.main.model.dto

import java.time.LocalDateTime

data class BlackListRequest(
    val userId: Int,
    val fromDate: LocalDateTime,
    val tillDate: LocalDateTime,
    val reason: String?,
)