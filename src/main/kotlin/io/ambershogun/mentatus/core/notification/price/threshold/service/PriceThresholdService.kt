package io.ambershogun.mentatus.core.notification.price.threshold.service

import io.ambershogun.mentatus.core.notification.price.threshold.EquitySign
import io.ambershogun.mentatus.core.notification.price.threshold.PriceThreshold
import io.ambershogun.mentatus.core.notification.price.threshold.exception.StockNotFoundException
import io.ambershogun.mentatus.core.notification.price.threshold.repo.PriceThresholdRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PriceThresholdService(
        private var thresholdRepository: PriceThresholdRepository,
        private var stockService: StockService
) {
    fun findAllByUser(chatId: Long): List<PriceThreshold> {
        return thresholdRepository.findAllByChatId(chatId)
    }

    fun getDistinctTickers(): MutableList<String> {
        return thresholdRepository.findAll().map { n -> n.ticker }.distinct().toMutableList()
    }

    fun findNotifications(ticker: String, equitySign: EquitySign, currentPrice: Double): List<PriceThreshold> {
        return when (equitySign) {
            EquitySign.GREATER -> thresholdRepository.findByTickerAndPriceLessThen(ticker, EquitySign.GREATER, currentPrice)
            EquitySign.LESS -> thresholdRepository.findByTickerAndPriceGreaterThen(ticker, EquitySign.LESS, currentPrice)
        }
    }

    fun delete(notifications: List<PriceThreshold>) {
        thresholdRepository.deleteAll(notifications)
    }

    fun createNotification(chatId: Long, inputMessage: String): PriceThreshold {
        val parts = inputMessage.split(EquitySign.LESS.sign, EquitySign.GREATER.sign)

        val price = BigDecimal.valueOf(parts[1].trim().replace(",", ".").toDouble()).setScale(2, RoundingMode.FLOOR)

        val ticker = parts[0].trim().toUpperCase()

        val stock = stockService.getStock(ticker)
                ?: throw StockNotFoundException(ticker)

        return thresholdRepository.save(
                PriceThreshold(
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
