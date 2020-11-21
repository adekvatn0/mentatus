package io.ambershogun.mentatus.notification.price

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "PriceNotification")
class PriceNotification(
        var chatId: Long,
        var ticker: String,
        var equitySign: EquitySign,
        var price: BigDecimal
) {

    override fun toString(): String {
        return "`${ticker} ${equitySign.text} ${price}`"
    }
}