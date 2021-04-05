package io.ambershogun.mentatus.core.notification.price

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.notification.price.threshold.EquitySign
import io.ambershogun.mentatus.core.notification.price.threshold.PriceThreshold
import io.ambershogun.mentatus.core.notification.price.threshold.repo.PriceThresholdRepository
import io.ambershogun.mentatus.core.notification.price.volatility.repo.PriceVolatilityRepository
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import yahoofinance.Stock
import yahoofinance.quotes.stock.StockQuote
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

class PriceScheduledServiceUnitTest : AbstractTest() {

    @Autowired
    lateinit var priceScheduledService: PriceScheduledService

    @Autowired
    lateinit var messageSource: MessageSource

    @Autowired
    lateinit var priceVolatilityRepository: PriceVolatilityRepository

    @MockBean
    lateinit var mentatusBot: MentatusBot

    @Captor
    lateinit var chatIdCaptor: ArgumentCaptor<Long>

    @Captor
    lateinit var messateCaptor: ArgumentCaptor<String>

    override fun init() {
        super.init()
        priceVolatilityRepository.deleteAll()
    }

    @Test
    fun `test trigger threshold notification`() {
        userRepository.save(User(1))

        priceThresholdRepository.save(PriceThreshold(1, "NOK", EquitySign.GREATER, 5.0, "USD"))

        val stock = Stock("NOK").apply {
            quote = StockQuote("NOK").apply {
                price = BigDecimal(10)
                currency = "USD"
            }
        }

        Mockito.`when`(stockService.getStocks(Mockito.any())).thenReturn(
                mutableMapOf("NOK" to stock)
        )

        priceScheduledService.triggerNotifications()

        Mockito.verify(mentatusBot).sendMessageText(
                chatIdCaptor.capture(),
                messateCaptor.capture()
        )

        val expectedMessage = messageSource.getMessage(
                "notification.threshold",
                arrayOf("NOK", 10, "USD"),
                Locale.forLanguageTag("ru")
        )
        assertEquals(1, chatIdCaptor.value)
        assertEquals(expectedMessage, messateCaptor.value)
    }


    @Test
    fun `test trigger volatility notification`() {
        userRepository.save(
                User(1).apply {
                    favoriteTickers = mutableSetOf("NOK")
                    settings = mutableMapOf(
                            Setting.PRICE_ALERT to true
                    )
                }
        )

        val stock = Stock("NOK").apply {
            quote = StockQuote("NOK").apply {
                price = BigDecimal(10)
                previousClose = BigDecimal(5)
                currency = "USD"
            }
        }

        Mockito.`when`(stockService.getStocks(Mockito.any())).thenReturn(
                mutableMapOf("NOK" to stock)
        )

        priceScheduledService.triggerNotifications()

        Mockito.verify(mentatusBot).sendMessageText(
                chatIdCaptor.capture(),
                messateCaptor.capture()
        )

        val expectedMessage = messageSource.getMessage(
                "notification.volatility.up",
                arrayOf("NOK", 100),
                Locale.forLanguageTag("ru")
        )
        assertEquals(1, chatIdCaptor.value)
        assertEquals(expectedMessage, messateCaptor.value)
    }
}