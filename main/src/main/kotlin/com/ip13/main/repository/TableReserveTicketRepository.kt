package com.ip13.main.repository

import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.TableReserveStatus
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TableReserveTicketRepository : JpaRepository<TableReserveTicket, Int> {
    @Transactional
    @Modifying
    @Query(
        "update table_reserve_ticket set manager_id = :manager_id, manager_comment = :manager_comment, " +
                "status = :status where id = :table_reserve_ticket",
        nativeQuery = true
    )
    fun processReservationDto(
        @Param("table_reserve_ticket")
        tableReserveTicketId: Int,
        @Param("manager_id")
        managerId: Int,
        @Param("manager_comment")
        managerComment: String?,
        @Param("status")
        status: String,
    ): Int
}