package com.ip13.main

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class Main {
    @Autowired
    lateinit var discoveryClient: DiscoveryClient
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}