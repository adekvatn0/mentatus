package io.ambershogun.mentatus.core.entity.notification.price.repo

import io.ambershogun.mentatus.core.entity.notification.price.EquitySign
import io.ambershogun.mentatus.core.entity.notification.price.PriceNotification
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface PriceNotificationRepository : MongoRepository<PriceNotification, String> {
    fun findAllByChatId(chatId: Long): List<PriceNotification>
    fun deleteAllByChatId(chatId: Long)

    @Query(value = "{ 'ticker' : ?0, 'equitySign':  ?1, 'price':  {'\$gte': ?2}}")
    fun findByTickerAndPriceGreaterThen(ticker: String, equitySign: EquitySign, price: Double): List<PriceNotification>

    @Query(value = "{ 'ticker' : ?0, 'equitySign':  ?1, 'price':  {'\$lte': ?2}}")
    fun findByTickerAndPriceLessThen(ticker: String, equitySign: EquitySign, price: Double): List<PriceNotification>
}