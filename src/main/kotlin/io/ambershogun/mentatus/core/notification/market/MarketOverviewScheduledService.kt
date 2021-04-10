package io.ambershogun.mentatus.core.notification.market

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.util.MessageService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MarketOverviewScheduledService(
        private val userService: UserService,
        private val mentatusBot: MentatusBot,
        private val messageService: MessageService
) {

    @Scheduled(cron = "0 0 13,19,23 * * *")
    fun sendMarketOverview() {
        val marketOverviewMessage = messageService.createMarketOverviewMessage()
        val marketImages = messageService.createMarketImagesMessage()

        userService.findBySetting(Setting.MARKET_OVERVIEW)
                .forEach {
                    marketOverviewMessage.chatId = it.chatId.toString()
                    marketImages.chatId = it.chatId.toString()

                    mentatusBot.sendMessages(
                            listOf(marketOverviewMessage, marketImages)
                    )
                }
    }
}