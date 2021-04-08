package io.ambershogun.mentatus.core.notification.market

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.util.MessageService
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class MarketReviewScheduledService(
        private val userService: UserService,
        private val mentatusBot: MentatusBot,
        private val messageService: MessageService
) {

    @Scheduled(cron = "0 0 13,19,23 * * *")
    fun notifyWithMarketReview() {
        val text = messageService.createMarketReview()

        userService.findBySetting(Setting.MARKET_OVERVIEW)
                .forEach {
                    mentatusBot.sendMessageText(
                            it.chatId,
                            text)
                }
    }
}