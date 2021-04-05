package io.ambershogun.mentatus.core.notification.price.threshold.service

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import yahoofinance.Stock
import yahoofinance.YahooFinance
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Service
class StockService(
        private val messageSource: MessageSource
) {

    fun getStockInfo(stock: Stock): String {
        return messageSource.getMessage(
                "stock.info",
                arrayOf(
                        "${stock.name} (${stock.symbol})",
                        getPriceInfo(stock),
                        "${stock.quote.priceAvg50.setScale(2, RoundingMode.HALF_DOWN)} ${stock.currency}",
                        "${stock.quote.priceAvg200.setScale(2, RoundingMode.HALF_DOWN)} ${stock.currency}",
                        BigDecimal.valueOf(stock.quote.volume).setScale(2, RoundingMode.HALF_DOWN),
                        BigDecimal.valueOf(stock.quote.avgVolume).setScale(2, RoundingMode.HALF_DOWN)
                ),
                Locale.forLanguageTag("ru")
        )
    }

    fun getStockShortInfo(ticker: String): String {
        val stock = YahooFinance.get(ticker)

        val builder = StringBuilder()

        builder.append(stock.symbol)
        builder.append(" ")
        builder.append(getPriceInfo(stock))
        return builder.toString()
    }

    fun getPriceInfo(stock: Stock): String {
        val quote = stock.quote

        val builder = StringBuilder()

        builder.append(quote.price.setScale(2, RoundingMode.HALF_DOWN))
        builder.append(" ")
        builder.append(stock.currency)
        builder.append(" ")

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

    fun getStocks(tickers: Array<String>?): MutableMap<String, Stock> {
        return YahooFinance.get(tickers)
    }

    fun getStock(ticker: String): Stock? {
        val moscowExchangeStock = YahooFinance.get("$ticker.ME")
        if (moscowExchangeStock != null) {
            return moscowExchangeStock
        }

        val stock = YahooFinance.get(ticker)
        if (stock != null) {
            return stock
        }

        return null
    }
}
