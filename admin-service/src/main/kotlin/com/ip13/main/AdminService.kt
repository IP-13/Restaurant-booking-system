package com.ip13.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdminService

fun main(args: Array<String>) {
    runApplication<AdminService>(*args)
}