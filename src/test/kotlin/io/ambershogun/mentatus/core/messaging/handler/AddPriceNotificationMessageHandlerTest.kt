package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.notification.price.EquitySign
import io.ambershogun.mentatus.core.entity.notification.price.PriceNotification
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.handler.message.AddPriceNotificationMessageHandler
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddPriceNotificationMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: AddPriceNotificationMessageHandler

    override fun `test message regex`() {
        assertTrue(("nok < 5".matches(Regex(messageHandler.messageRegEx()))))
        assertTrue(("nok < 5.".matches(Regex(messageHandler.messageRegEx()))))
        assertTrue(("mrna<100").matches(Regex(messageHandler.messageRegEx())))
        assertTrue(("MRNA  >   100.0").matches(Regex(messageHandler.messageRegEx())))
        assertTrue(("MrNa<  100,000").matches(Regex(messageHandler.messageRegEx())))

        assertFalse(("100 > mrna").matches(Regex(messageHandler.messageRegEx())))
        assertFalse(("ЯЮЫБ < 100").matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val update = Update().apply { message = Message().apply { text = "mrna > 100" } }

        val response = messageHandler.handleMessage(User(1), update)
        assertEquals(
                messageSource.getMessage("notification.add",
                        arrayOf(PriceNotification(1, "MRNA", EquitySign.GREATER, 100.00, "USD")),
                        Locale.forLanguageTag("ru")
                ),
                (response[0] as SendMessage).text
        )
    }

    @Test
    fun `test stock not found by ticker`() {
        Mockito.`when`(stockService.getStock(anyString())).thenReturn(null)

        val update = Update().apply { message = Message().apply { text = "mrna > 100" } }

        val response = messageHandler.handleMessage(User(1), update)
        assertEquals(
                messageSource.getMessage("stock.not.found", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )
    }
}