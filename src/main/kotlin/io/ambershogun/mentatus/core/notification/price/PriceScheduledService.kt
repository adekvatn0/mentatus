package io.ambershogun.mentatus.core.notification.price

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.notification.price.threshold.EquitySign
import io.ambershogun.mentatus.core.notification.price.threshold.service.PriceThresholdService
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import io.ambershogun.mentatus.core.notification.price.volatility.PriceVolatility
import io.ambershogun.mentatus.core.notification.price.volatility.repo.PriceVolatilityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import yahoofinance.Stock
import java.math.BigDecimal
import java.util.*
import kotlin.math.abs

@Service
class PriceScheduledService(
        private var stockService: StockService,
        private var thresholdService: PriceThresholdService,
        private val userService: UserService,
        private var bot: MentatusBot,
        @Value("\${notification.volatility.trigger-percent}")
        private val volatilityTriggerPercent: Double,
        @Value("\${notification.volatility.step-percent}")
        private val volatilityStepPercent: Double,
        private val messageSource: MessageSource,
        private val priceVolatilityRepository: PriceVolatilityRepository
) {

    @Scheduled(fixedRateString = "\${notification.update-rate-millis}")
    fun triggerNotifications() {
        val notificationTickers = thresholdService.getDistinctTickers()
        if (!CollectionUtils.isEmpty(notificationTickers)) {
            stockService.getStocks(notificationTickers.toTypedArray()).values.forEach { stock ->
                val ticker = stock.symbol

                triggerThresholdNotifications(ticker, stock.quote.price, EquitySign.GREATER, stock.currency)
                triggerThresholdNotifications(ticker, stock.quote.price, EquitySign.LESS, stock.currency)
            }
        }

        val favoriteTickers = userService.findAll().map {
            it.favoriteTickers
        }.flatten()
        val users = userService.findBySetting(Setting.PRICE_ALERT)
        if (!CollectionUtils.isEmpty(favoriteTickers)) {
            stockService.getStocks(favoriteTickers.toTypedArray()).values.forEach { stock ->
                triggerVolatilityNotifications(stock, users)
            }
        }
    }

    private fun triggerVolatilityNotifications(stock: Stock, users: List<User>) {
        val percentChange = getPercentChange(stock)
        val absPercentChange = abs(percentChange)

        if (absPercentChange >= volatilityTriggerPercent) {
            var volatility = priceVolatilityRepository.findByTicker(stock.symbol)
            if (volatility != null) {
                if (volatility.percent > absPercentChange) {
                    return
                } else if (volatility.percent + volatilityStepPercent > absPercentChange) {
                    return
                }
            } else {
                volatility = PriceVolatility(stock.symbol)
            }

            volatility.percent = absPercentChange
            priceVolatilityRepository.save(volatility)

            val messageName = if (percentChange > 0) {
                "notification.volatility.up"
            } else {
                "notification.volatility.down"
            }

            val notificationMessage = messageSource.getMessage(
                    messageName,
                    arrayOf(stock.symbol, absPercentChange),
                    Locale.forLanguageTag("ru")
            )

            users.filter {
                it.favoriteTickers.contains(stock.symbol)
            }.forEach {
                bot.sendMessageText(it.chatId, notificationMessage)
            }
        }
    }

    private fun getPercentChange(stock: Stock): Double {
        return (stock.quote.price.toDouble() - stock.quote.previousClose.toDouble()) / stock.quote.previousClose.toDouble() * 100
    }

    private fun triggerThresholdNotifications(ticker: String, currentPrice: BigDecimal, equitySign: EquitySign, currency: String) {
        val notifications = thresholdService.findNotifications(ticker, equitySign, currentPrice.toDouble())

        notifications.forEach { notification ->
            val notificationMessage = messageSource.getMessage(
                    "notification.threshold",
                    arrayOf(notification.ticker, currentPrice.setScale(2), currency),
                    Locale.forLanguageTag("ru")
            )
            bot.sendMessageText(notification.chatId, notificationMessage)
        }

        thresholdService.delete(notifications)
    }
}