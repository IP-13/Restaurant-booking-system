package com.ip13.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReservationService

fun main(args: Array<String>) {
    runApplication<ReservationService>(*args)
}