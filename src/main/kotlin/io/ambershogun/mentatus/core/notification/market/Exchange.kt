package io.ambershogun.mentatus.core.notification.market

enum class Exchange(var prettyName: String, var ticker: String) {
    USDRUB("\uD83D\uDCB5 USD/RUB", "RUB=X"),
    EURRUB("\uD83D\uDCB6 EUR/RUB", "EURRUB=X");

    companion object {
        private val map = values().associateBy(Exchange::ticker)
        fun findByTicker(ticker: String) = map[ticker]
    }
}