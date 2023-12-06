package com.ip13.main.exceptionHandling.exception

class TableReserveTicketNotFound(
    override val message: String = "TableReserveTicket not found",
) : NotFoundException(message)