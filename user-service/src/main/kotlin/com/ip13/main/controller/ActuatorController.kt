package com.ip13.main.controller

import com.ip13.main.util.WorkloadEmulator
import com.ip13.main.util.getLogger
import org.springframework.boot.actuate.beans.BeansEndpoint
import org.springframework.boot.actuate.beans.BeansEndpoint.BeansDescriptor
import org.springframework.boot.actuate.flyway.FlywayEndpoint
import org.springframework.boot.actuate.flyway.FlywayEndpoint.FlywayBeansDescriptor
import org.springframework.boot.actuate.health.HealthComponent
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.actuate.metrics.MetricsEndpoint
import org.springframework.cloud.endpoint.RefreshEndpoint
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user-actuator")
class ActuatorController(
    private val refreshEndpoint: RefreshEndpoint,
    private val healthEndpoint: HealthEndpoint,
    private val beansEndpoint: BeansEndpoint,
    private val flywayEndpoint: FlywayEndpoint,
    private val metricsEndpoint: MetricsEndpoint,
    private val workloadEmulator: WorkloadEmulator,
    private val infoEndpoint: InfoEndpoint,
) {
    private val log = getLogger(javaClass)

    @GetMapping("/refresh")
    fun refresh(): Collection<String> {
        log.debug("old fail-probability: {}", workloadEmulator.probability)
        val refreshed = refreshEndpoint.refresh()
        log.debug("updated fail-probability: {}", workloadEmulator.probability)
        return refreshed
    }

    @GetMapping("/health")
    fun health(): HealthComponent {
        return healthEndpoint.health()
    }

    @GetMapping("/beans")
    fun beans(): BeansDescriptor {
        return beansEndpoint.beans()
    }

    @GetMapping("/flyway")
    fun flyway(): FlywayBeansDescriptor {
        return flywayEndpoint.flywayBeans()
    }

    @GetMapping("/metrics")
    fun metrics(): MetricsEndpoint.MetricNamesDescriptor {
        return metricsEndpoint.listNames()
    }

    @GetMapping("/info")
    fun info(): Map<String, Any> {
        return infoEndpoint.info()
    }
}