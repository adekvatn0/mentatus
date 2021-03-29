package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import yahoofinance.quotes.stock.StockQuote
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class StockInfoMessageHandler(
        private val stockService: StockService
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "^\\w+(\\s+|)\\?$"
    }

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        val stock = stockService.getStock(getTicker(inputMessage)) ?: return listOf(
                responseService.createSendMessage(user.chatId.toString(), "stock.not.found")
        )

        val sendMessage = responseService.createSendMessage(
                user.chatId.toString(),
                "stock.info",
                "${stock.name} (${stock.symbol})",
                "${stock.quote.price} ${stock.currency} ${formatPriceChangeWithEmoji(stock.quote)}",
                "${stock.quote.priceAvg50} ${stock.currency}",
                "${stock.quote.priceAvg200} ${stock.currency}",
                stock.quote.volume,
                stock.quote.avgVolume
        )

        return listOf(sendMessage)
    }

    private fun formatPriceChangeWithEmoji(quote: StockQuote): String {
        val builder = StringBuilder()

        val priceChange = quote.price - quote.previousClose
        val percentChange = (quote.price.toDouble() - quote.previousClose.toDouble()) / quote.previousClose.toDouble() * 100
        if (percentChange < 0) {
            builder.append("\uD83D\uDD34")
        } else {
            builder.append("\uD83D\uDFE2")
        }

        builder.append(priceChange)
        builder.append(" ")
        builder.append("(${BigDecimal.valueOf(percentChange).setScale(2, RoundingMode.HALF_DOWN)}%)")

        return builder.toString()
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").dropLast(1)
    }

    override fun isRetryable() = true
}