package com.ip13.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}