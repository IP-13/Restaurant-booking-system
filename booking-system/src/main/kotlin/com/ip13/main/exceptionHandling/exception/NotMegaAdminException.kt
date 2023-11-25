package com.ip13.main.exceptionHandling.exception

class NotMegaAdminException(
    override val message: String = "You are not mega admin",
) : Exception(message)