package io.ambershogun.mentatus.notification.price

import io.ambershogun.mentatus.MentatusBot
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ScheduledNotificationService(
        private var stockService: StockService,
        private var notificationService: PriceNotificationService,
        private var bot: MentatusBot
) {
    private val trackedStocks: HashSet<String> = HashSet()

    @Scheduled(fixedRateString = "\${notification.update-rate-millis}")
    fun updateStockDataAndTriggerNotifications() {
        val tickers = notificationService.getDistinctTickers()

        if (tickers.isEmpty()) {
            return
        }

        val stocks = stockService.getStocks(tickers.toTypedArray())
        stocks.values.forEach { stock ->
            val ticker = stock.symbol
            if (trackedStocks.contains(ticker)) {
                notifyUsersAndDeleteNotifications(ticker, stock.quote.price, stock.quote.dayHigh, EquitySign.GREATER, stock.currency)
                notifyUsersAndDeleteNotifications(ticker, stock.quote.price, stock.quote.dayLow, EquitySign.LESS, stock.currency)
            }

            trackedStocks.add(ticker)
        }
    }

    private fun notifyUsersAndDeleteNotifications(ticker: String, currentPrice: BigDecimal, priceToCompareWith: BigDecimal, equitySign: EquitySign, currency: String) {
        val notifications = notificationService.findNotifications(ticker, priceToCompareWith, equitySign)

        notifications.forEach { notification ->
            bot.sendMessageText(
                    notification.chatId,
                    "Цена за акцию `${notification.ticker}` достигла `$currentPrice $currency`"
            )
        }

        notificationService.delete(notifications)
    }
}