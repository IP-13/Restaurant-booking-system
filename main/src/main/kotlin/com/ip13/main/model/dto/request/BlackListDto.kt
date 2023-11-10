package com.ip13.main.model.dto.request

import java.time.LocalDateTime

data class BlackListDto(
    val userId: Int,
    val fromDate: LocalDateTime,
    val tillDate: LocalDateTime,
    val reason: String?,
)