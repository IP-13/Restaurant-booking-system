package com.ip13.main.exceptionHandling.exception

class TableReserveTicketNotFoundException(
    override val message: String = "Table reserve ticket not found",
) : NotFoundException(message)