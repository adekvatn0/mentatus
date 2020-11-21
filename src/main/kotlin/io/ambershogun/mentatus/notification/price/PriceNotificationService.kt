package io.ambershogun.mentatus.notification.price

import io.ambershogun.mentatus.message.handler.AddPriceNotificationMessageHandler
import io.ambershogun.mentatus.notification.price.exception.StockNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PriceNotificationService(
        private var notificationRepository: PriceNotificationRepository,
        private var stockService: StockService
) {
    fun findAllByUser(chatId: Long): List<PriceNotification> {
        return notificationRepository.findAllByChatId(chatId)
    }

    fun deleteAllByUser(chatId: Long) {
        notificationRepository.deleteAllByChatId(chatId)
    }

    fun getDistinctTickers(): List<String> {
        return notificationRepository.findAll().map { n -> n.ticker }.distinct().toList()
    }

    fun findNotifications(ticker: String, currentPrice: BigDecimal, equitySign: EquitySign): List<PriceNotification> {
        return when (equitySign) {
            EquitySign.GREATER -> notificationRepository.findByTickerAndPriceLessThen(ticker, EquitySign.GREATER, currentPrice)
            EquitySign.LESS -> notificationRepository.findByTickerAndPriceGreaterThen(ticker, EquitySign.LESS, currentPrice)
        }
    }

    fun delete(notifications: List<PriceNotification>) {
        notificationRepository.deleteAll(notifications)
    }

    fun createNotification(chatId: Long, inputMessage: String): PriceNotification {
        val parts = inputMessage.split(AddPriceNotificationMessageHandler.LESS_DELIMITER, AddPriceNotificationMessageHandler.GREATER_DELIMITER)

        val price = BigDecimal.valueOf(parts[1].trim().replace(",", ".").toDouble()).setScale(2, RoundingMode.FLOOR)

        val ticker = parts[0].trim().toUpperCase()

        val yahooFinanceTickerName = stockService.getYahooFinanceTickerName(ticker)
                ?: throw StockNotFoundException(ticker)

        return notificationRepository.save(
                PriceNotification(
                        chatId,
                        yahooFinanceTickerName,
                        getEquitySign(inputMessage),
                        price
                )
        )
    }

    fun getEquitySign(inputMessage: String): EquitySign {
        if (inputMessage.contains(AddPriceNotificationMessageHandler.LESS_DELIMITER)) {
            return EquitySign.LESS
        }

        if (inputMessage.contains(AddPriceNotificationMessageHandler.GREATER_DELIMITER)) {
            return EquitySign.GREATER
        }

        throw IllegalStateException()
    }
}
