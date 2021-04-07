package io.ambershogun.mentatus.core.notification.indexes

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.util.MessageService
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class IndexesScheduledService(
        private val userService: UserService,
        private val mentatusBot: MentatusBot,
        private val stockService: StockService,
        private val messageService: MessageService
) {

    @Scheduled(cron = "0 30 19 * * *")
    fun notifyWithMarketReview() {
        val indexTickers = Index.values().map {
            it.ticker
        }.toTypedArray()

        val stocks = stockService.getStocks(indexTickers)

        val text = messageService.createIndexesText(stocks)

        userService.findBySetting(Setting.MARKET_OVERVIEW)
                .forEach {
                    mentatusBot.sendMessageText(
                            it.chatId,
                            text)
                }
    }
}