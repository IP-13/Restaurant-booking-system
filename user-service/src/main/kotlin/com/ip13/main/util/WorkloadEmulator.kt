package com.ip13.main.util

import jakarta.ws.rs.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Component
import kotlin.random.Random

@RefreshScope
@Component
class WorkloadEmulator {
    private val log = getLogger(javaClass)

    @Value("\${emulate-workload.fail-probability}")
    lateinit var probability: String

    /**
     * Emulate workload to test circuit-breaker
     * @throws BadRequestException
     */
    fun emulateWorkload() {
        log.debug("fail-probability: {}", probability)

        if (probability.toDouble() > Random.nextDouble()) {
            log.debug("Workload is too high. Server cannot handle it")
            throw BadRequestException()
        }

        log.debug("Server is powerful enough to handle workload")
    }
}