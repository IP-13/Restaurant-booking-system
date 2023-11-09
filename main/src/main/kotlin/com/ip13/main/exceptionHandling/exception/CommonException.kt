package com.ip13.main.exceptionHandling.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
open class CommonException(
    override val message: String,
    open val httpStatusCode: HttpStatusCode,
) : Exception(message)