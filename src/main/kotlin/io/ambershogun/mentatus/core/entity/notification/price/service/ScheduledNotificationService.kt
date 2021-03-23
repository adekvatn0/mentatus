package io.ambershogun.mentatus.core.entity.notification.price.service

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.notification.price.EquitySign
import org.springframework.context.MessageSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ScheduledNotificationService(
        private var stockService: StockService,
        private var notificationService: PriceNotificationService,
        private var bot: MentatusBot,
        private var messageSource: MessageSource
) {

    @Scheduled(fixedRateString = "\${notification.update-rate-millis}")
    fun updateStockDataAndTriggerNotifications() {
        val tickers = notificationService.getDistinctTickers()

        if (tickers.isEmpty()) {
            return
        }

        val stocks = stockService.getStocks(tickers.toTypedArray())
        stocks.values.forEach { stock ->
            val ticker = stock.symbol
            notifyUsersAndDeleteNotifications(ticker, stock.quote.price, EquitySign.GREATER, stock.currency)
            notifyUsersAndDeleteNotifications(ticker, stock.quote.price, EquitySign.LESS, stock.currency)
        }
    }

    private fun notifyUsersAndDeleteNotifications(ticker: String, currentPrice: BigDecimal, equitySign: EquitySign, currency: String) {
        val notifications = notificationService.findNotifications(ticker, equitySign, currentPrice.toDouble())

        notifications.forEach { notification ->
            bot.sendMessageText(
                    notification.chatId,
                    "Цена за акцию `${notification.ticker}` достигла `${currentPrice.setScale(2)} $currency`"
            )
        }

        notificationService.delete(notifications)
    }
}