package io.ambershogun.mentatus.notification.price

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

    fun findNotifications(ticker: String, equitySign: EquitySign, currentPrice: Double): List<PriceNotification> {
        return when (equitySign) {
            EquitySign.GREATER -> notificationRepository.findByTickerAndPriceLessThen(ticker, EquitySign.GREATER, currentPrice)
            EquitySign.LESS -> notificationRepository.findByTickerAndPriceGreaterThen(ticker, EquitySign.LESS, currentPrice)
        }
    }

    fun delete(notifications: List<PriceNotification>) {
        notificationRepository.deleteAll(notifications)
    }

    fun createNotification(chatId: Long, inputMessage: String): PriceNotification {
        val parts = inputMessage.split(EquitySign.LESS.sign, EquitySign.GREATER.sign)

        val price = BigDecimal.valueOf(parts[1].trim().replace(",", ".").toDouble()).setScale(2, RoundingMode.FLOOR)

        val ticker = parts[0].trim().toUpperCase()

        val stock = stockService.getStock(ticker)
                ?: throw StockNotFoundException(ticker)

        return notificationRepository.save(
                PriceNotification(
                        chatId,
                        stock.symbol,
                        getEquitySign(inputMessage),
                        price.toDouble(),
                        stock.currency
                )
        )
    }

    fun getEquitySign(inputMessage: String): EquitySign {
        if (inputMessage.contains(EquitySign.LESS.sign)) {
            return EquitySign.LESS
        }

        if (inputMessage.contains(EquitySign.GREATER.sign)) {
            return EquitySign.GREATER
        }

        throw IllegalStateException()
    }
}
