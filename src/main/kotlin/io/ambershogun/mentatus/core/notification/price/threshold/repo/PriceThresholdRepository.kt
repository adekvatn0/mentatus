package io.ambershogun.mentatus.core.notification.price.threshold.repo

import io.ambershogun.mentatus.core.notification.price.threshold.EquitySign
import io.ambershogun.mentatus.core.notification.price.threshold.PriceThreshold
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface PriceThresholdRepository : MongoRepository<PriceThreshold, String> {
    fun findAllByChatId(chatId: Long): List<PriceThreshold>

    @Query(value = "{ 'ticker' : ?0, 'equitySign':  ?1, 'price':  {'\$gte': ?2}}")
    fun findByTickerAndPriceGreaterThen(ticker: String, equitySign: EquitySign, price: Double): List<PriceThreshold>

    @Query(value = "{ 'ticker' : ?0, 'equitySign':  ?1, 'price':  {'\$lte': ?2}}")
    fun findByTickerAndPriceLessThen(ticker: String, equitySign: EquitySign, price: Double): List<PriceThreshold>
}