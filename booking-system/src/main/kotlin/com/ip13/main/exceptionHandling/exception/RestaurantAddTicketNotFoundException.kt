package com.ip13.main.exceptionHandling.exception

class RestaurantAddTicketNotFoundException(
    override val message: String = "RestaurantAddTicket not found",
) : NotFoundException(message)