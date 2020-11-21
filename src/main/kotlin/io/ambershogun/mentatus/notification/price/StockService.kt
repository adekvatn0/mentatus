package io.ambershogun.mentatus.notification.price

import org.springframework.stereotype.Service
import yahoofinance.Stock
import yahoofinance.YahooFinance

@Service
class StockService {

    fun getStocks(tickers: Array<String>): MutableMap<String, Stock> {
        return YahooFinance.get(tickers)
    }

    fun getYahooFinanceTickerName(ticker: String): String? {
        val stock = YahooFinance.get(ticker)
        if (stock != null) {
            return stock.symbol
        }

        val moscowExchangeStock = YahooFinance.get("$ticker.ME")
        if (moscowExchangeStock != null) {
            return moscowExchangeStock.symbol
        }

        return null
    }
}
