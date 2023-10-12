package com.ip13.main.controller

import com.ip13.main.model.MyEntity
import com.ip13.main.repository.ReserveRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReserveController(
    val reserveRepository: ReserveRepository,
) {
    @GetMapping("/reserve-table")
    fun reserveTable(
        @RequestParam a: Int,
        @RequestParam b: Int,
    ): String {
        reserveRepository.save(MyEntity(a, b))
        return "a: ${a + 3}, b : ${b.toDouble() * 0.3}"
    }
}