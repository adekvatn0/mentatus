package io.ambershogun.mentatus.message.handler

import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddPriceNotificationMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: AddPriceNotificationMessageHandler

    override fun `test message regex`() {
        assertTrue(("mrna<100").matches(Regex(messageHandler.messageRegEx())))
        assertTrue(("MRNA  >   100.0").matches(Regex(messageHandler.messageRegEx())))
        assertTrue(("MrNa<  100,000").matches(Regex(messageHandler.messageRegEx())))

        assertFalse(("100 > mrna").matches(Regex(messageHandler.messageRegEx())))
        assertFalse(("ЯЮЫБ < 100").matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "ru", "mrna > 100")
        assertEquals(
                messageSource.getMessage("notification.add", arrayOf("MRNA", BigDecimal.valueOf(100.00)), Locale.forLanguageTag("ru")),
                response.text
        )
    }

    @Test
    fun `test stock not found by ticker`() {
        Mockito.`when`(stockService.getYahooFinanceTickerName(anyString())).thenReturn(null)

        val response = messageHandler.handleMessage(1, "ru", "mrna > 100")
        assertEquals(
                messageSource.getMessage("notification.add.stock.not.found", emptyArray(), Locale.forLanguageTag("ru")),
                response.text
        )
    }
}