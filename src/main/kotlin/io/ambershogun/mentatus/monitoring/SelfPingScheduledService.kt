package io.ambershogun.mentatus.monitoring

import io.ambershogun.mentatus.core.properties.AppProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SelfPingScheduledService(
        private val appProperties: AppProperties,
        restTemplateBuilder: RestTemplateBuilder
) {

    private val logger = LoggerFactory.getLogger("health")

    private val restTemplate = restTemplateBuilder.build()

    @Scheduled(fixedRate = 1000 * 60 * 5)
    fun pingSelf() {
        val response = restTemplate.getForEntity(appProperties.url, String::class.java)
        logger.debug(response.body)
    }
}