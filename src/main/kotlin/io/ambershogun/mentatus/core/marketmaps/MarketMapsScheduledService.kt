package io.ambershogun.mentatus.core.marketmaps

import io.ambershogun.mentatus.core.marketmaps.selenium.SeleniumService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class MarketMapsScheduledService(
        private val seleniumService: SeleniumService
) {

    private val logger = LoggerFactory.getLogger("messaging")

    @Scheduled(fixedRate = 1000 * 60 * 5)
    fun updateMarketMaps() {
        try {
            seleniumService.updateFinvizScreenshots()
        } catch (e: Exception) {
            logger.error("Failed to update market maps\n\n", e)
        }
    }
}