package io.ambershogun.mentatus.core.notification.price.volatility

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "PriceVolatility")
data class PriceVolatility(
        val ticker: String
) {
    @Id
    var id: String? = null
    var percent: Double = 0.0
}
