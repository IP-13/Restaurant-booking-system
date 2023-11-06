package com.ip13.main.exceptionHandling.exception

class ManagerNotFoundException(
    override val message: String = "Manager not found",
) : NotFoundException(message)