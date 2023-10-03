package com.ip13.main.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReserveController {
    @GetMapping("/reserve-table")
    fun reserveTable(): String {
        return "table reserved"
    }
}