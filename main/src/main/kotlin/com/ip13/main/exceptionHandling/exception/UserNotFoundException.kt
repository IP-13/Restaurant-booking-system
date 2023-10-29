package com.ip13.main.exceptionHandling.exception

class UserNotFoundException(
    override val message: String = "User not found",
) : Exception()