package io.ambershogun.mentatus.core.notification.price.volatility.repo

import io.ambershogun.mentatus.core.notification.price.volatility.PriceVolatility
import org.springframework.data.mongodb.repository.MongoRepository

interface PriceVolatilityRepository : MongoRepository<PriceVolatility, String> {
    fun findByTicker(ticker: String): PriceVolatility?
}