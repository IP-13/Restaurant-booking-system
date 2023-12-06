package com.ip13.main.util

import jakarta.ws.rs.BadRequestException
import org.springframework.beans.factory.annotation.Value
import kotlin.random.Random


@Value("emulate-workload.fail-probability")
private lateinit var probability: String

/**
 * Emulate workload to test circuit-breaker
 * @throws BadRequestException
 */
fun emulateWorkload() {
    if (probability.toFloat() < Random.nextFloat()) {
        throw BadRequestException()
    }
}