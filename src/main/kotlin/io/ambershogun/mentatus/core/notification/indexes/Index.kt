package io.ambershogun.mentatus.core.notification.indexes

enum class Index(var prettyName: String, var ticker: String) {
    SP500("S&P 500", "^GSPC"),
    NASDAQ("Nasdaq", "^IXIC"),
    DJI("Dow Jones", "^DJI"),
    RTS("RTS", "RTSI.ME"),
    NIKKEI("Nikkei", "^N225"),
    VIX("VIX", "^VIX");

    companion object {
        private val map = values().associateBy(Index::ticker)
        fun findByTicker(ticker: String) = map[ticker]
    }
}