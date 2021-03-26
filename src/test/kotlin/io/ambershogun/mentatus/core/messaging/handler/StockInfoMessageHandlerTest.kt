package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.messaging.handler.message.StockInfoMessageHandler
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import yahoofinance.Stock
import yahoofinance.quotes.stock.StockQuote
import java.math.BigDecimal
import java.util.*

class StockInfoMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: StockInfoMessageHandler

    override fun `test message regex`() {
        Assertions.assertTrue("nok?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertTrue("NOK?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertTrue("NOK ?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        Mockito.`when`(stockService.getStock(ArgumentMatchers.anyString())).thenAnswer {
            val args: Array<Any> = it.arguments
            return@thenAnswer Stock((args[0] as String).toUpperCase()).apply {
                currency = "USD"
                name = "Nokia"
                quote = StockQuote((args[0] as String).toUpperCase()).apply {
                    price = BigDecimal.valueOf(100)
                    priceAvg50 = BigDecimal.valueOf(50)
                    priceAvg200 = BigDecimal.valueOf(200)
                    volume = 1000
                    avgVolume = 2000
                }
            }
        }

        val response = messageHandler.handleMessage(1, "nok?")
        kotlin.test.assertEquals(
                messageSource.getMessage(
                        "stock.info",
                        arrayOf(
                                "Nokia (NOK)",
                                "${BigDecimal.valueOf(100)} USD",
                                "${BigDecimal.valueOf(50)} USD",
                                "${BigDecimal.valueOf(200)} USD",
                                1000L,
                                2000L
                        ),
                        Locale.forLanguageTag("ru")
                ),
                (response[0] as SendMessage).text
        )
    }
}