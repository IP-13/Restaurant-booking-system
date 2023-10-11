package com.ip13.main.controller

import com.ip13.main.repository.ReserveRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReserveController(
    reserveRepository: ReserveRepository,
) {
    @GetMapping("/reserve-table")
    fun reserveTable(
        @RequestParam a: Int,
    ): String {
        return "${a + 10}"
    }
}