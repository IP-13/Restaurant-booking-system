package com.ip13.main.exceptionHandling.exception

import org.springframework.http.HttpStatusCode

class AttemptToOverthrowMegaAdminException(
    override val message: String,
    override val httpStatusCode: HttpStatusCode,
) : CommonException(message, httpStatusCode)