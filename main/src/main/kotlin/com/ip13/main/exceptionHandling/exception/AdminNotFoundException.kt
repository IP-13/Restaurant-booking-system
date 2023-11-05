package com.ip13.main.exceptionHandling.exception

class AdminNotFoundException(
    override val message: String = "Admin not found",
) : NotFoundException(message)