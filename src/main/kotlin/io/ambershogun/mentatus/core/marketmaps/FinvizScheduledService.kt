package io.ambershogun.mentatus.core.marketmaps

import io.ambershogun.mentatus.core.marketmaps.selenium.SeleniumService
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class FinvizScheduledService(
        private val seleniumService: SeleniumService
) {

    @Scheduled(fixedRate = 1000 * 60 * 5)
    fun updateMarketMaps() {
//        seleniumService.updateFinvizScreenshots()
    }
}