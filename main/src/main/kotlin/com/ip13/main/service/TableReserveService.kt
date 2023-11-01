package com.ip13.main.service

import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.repository.TableReserveTicketRepository
import org.springframework.stereotype.Service

@Service
class TableReserveService(
    val tableReserveTicketRepository: TableReserveTicketRepository,
) {
    fun save(tableReserveTicket: TableReserveTicket): Int {
        return tableReserveTicketRepository.save(tableReserveTicket).id
    }
}