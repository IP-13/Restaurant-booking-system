package com.ip13.main.event

data class BlackListNotificationEvent(
    val senderName: String,
    val receiverName: String,
    val message: String,
)
