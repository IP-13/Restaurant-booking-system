package com.ip13.main.exceptionHandling.exception

class TableReserveTicketNotFoundException(
    override val message: String = "TableReserveTicket not found",
) : NotFoundException(message)