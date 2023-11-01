package com.ip13.main.repository

import com.ip13.main.model.entity.TableReserveTicket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TableReserveTicketRepository : JpaRepository<TableReserveTicket, Int>