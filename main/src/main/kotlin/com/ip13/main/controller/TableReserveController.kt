package com.ip13.main.controller

import com.ip13.main.mapper.TableReserveTicketMapper
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.service.TableReserveService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reserve")
class TableReserveController(
    val tableReserveService: TableReserveService,
) {
    @PostMapping("reserve_table")
    fun reserveTable(
        @RequestBody
        tableReserveTicketDto: TableReserveTicketDto
    ): ResponseEntity<String> {
        val createdTicketId = tableReserveService.save(
            TableReserveTicketMapper.tableReserveTicketDtoToTableReserveTicket(
                tableReserveTicketDto
            )
        )

        return ResponseEntity(
            "Reserve ticket has been successfully created with id: $createdTicketId",
            HttpStatus.OK
        )
    }
}