package com.ip13.main.exceptionHandling.exception

class TokenNotFoundException(
    override val message: String = "User not found",
) : NotFoundException(message)