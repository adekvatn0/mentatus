package io.ambershogun.mentatus.core.notification.price.volatility

import io.ambershogun.mentatus.core.notification.price.volatility.repo.PriceVolatilityRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class PriceVolatilityScheduledService(
        private val priceVolatilityRepository: PriceVolatilityRepository
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 30 16 * * *", zone = "Europe/Moscow")
    fun deleteAllVolatilityRecords() {
        priceVolatilityRepository.deleteAll()
        logger.info("All PriceVolatility are deleted")
    }
}