package com.ip13.main.exceptionHandling.exception

class RestaurantNotFoundException(
    override val message: String = "Restaurant not found",
) : NotFoundException(message)