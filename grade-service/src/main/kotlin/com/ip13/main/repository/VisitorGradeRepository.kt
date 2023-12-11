package com.ip13.main.repository

import com.ip13.main.model.entity.VisitorGrade
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface VisitorGradeRepository : ReactiveCrudRepository<VisitorGrade, Int> {
    fun existsByTableReserveTicketId(tableReserveTicketId: Int): Mono<Boolean>
}