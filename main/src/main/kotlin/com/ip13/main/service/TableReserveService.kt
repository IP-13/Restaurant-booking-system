package com.ip13.main.service

import com.ip13.main.model.dto.ReservationProcessDto
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.repository.TableReserveTicketRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TableReserveService(
    private val tableReserveTicketRepository: TableReserveTicketRepository,
) {
    fun save(tableReserveTicket: TableReserveTicket): Int {
        return tableReserveTicketRepository.save(tableReserveTicket).id
    }

    fun getReservations(pageRequest: PageRequest): List<TableReserveTicket> {
        return tableReserveTicketRepository.findAll(pageRequest).toList()
    }

    fun processReservation(reservationProcessDto: ReservationProcessDto, managerId: Int): Int {
        return tableReserveTicketRepository.processReservationDto(
            tableReserveTicketId = reservationProcessDto.tableReserveTicketId,
            managerId = managerId,
            managerComment = reservationProcessDto.managerComment,
            status = reservationProcessDto.status.name,
        )
    }

    fun findByIdOrNull(tableReserveTicketId: Int): TableReserveTicket? {
        return tableReserveTicketRepository.findByIdOrNull(tableReserveTicketId)
    }
}