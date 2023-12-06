package com.ip13.main.util

import jakarta.ws.rs.BadRequestException
import org.springframework.beans.factory.annotation.Value
import kotlin.random.Random

object WorkloadEmulator {
    private val log = getLogger(javaClass)

    @Value("emulate-workload.fail-probability")
    private lateinit var probability: String

    /**
     * Emulate workload to test circuit-breaker
     * @throws BadRequestException
     */
    fun emulateWorkload() {
        if (probability.toFloat() < Random.nextFloat()) {
            log.debug("Workload is too high. Server cannot handle it")
            throw BadRequestException()
        }

        log.debug("Server is powerful enough to handle workload")
    }
}