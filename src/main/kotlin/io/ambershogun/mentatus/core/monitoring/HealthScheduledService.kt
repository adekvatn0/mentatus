package io.ambershogun.mentatus.core.monitoring

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class HealthScheduledService(
        restTemplateBuilder: RestTemplateBuilder
) {

    private val logger = LoggerFactory.getLogger("health")

    private val restTemplate = restTemplateBuilder.build()

    @Scheduled(fixedRate = 1000 * 60 * 15)
    fun checkHealth() {
        val status = restTemplate.getForObject("http://localhost:8080/actuator/health", Status::class.java)
        logger.debug(status!!.status)
    }
}

class Status {
    var status: String? = null
}