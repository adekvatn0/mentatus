package io.ambershogun.mentatus.core.notification.price.volatility

import io.ambershogun.mentatus.core.notification.price.volatility.repo.PriceVolatilityRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class PriceVolatilityScheduledService(
        private val priceVolatilityRepository: PriceVolatilityRepository
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteAllVolatilityRecords() {
        priceVolatilityRepository.deleteAll()
    }
}