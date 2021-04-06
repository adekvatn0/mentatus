package io.ambershogun.mentatus.core.notification.price.threshold.service

import org.springframework.stereotype.Service
import yahoofinance.Stock
import yahoofinance.YahooFinance

@Service
class StockService {

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
