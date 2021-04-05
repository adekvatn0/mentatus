package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.handler.message.StockInfoMessageHandler
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import kotlin.test.assertEquals

class StockInfoMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: StockInfoMessageHandler

    @Autowired
    lateinit var userService: UserService

    override fun `test message regex`() {
        Assertions.assertTrue("nok?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertTrue("NOK?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertTrue("NOK ?".matches(Regex(messageHandler.messageRegEx())))
        Assertions.assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
//        Mockito.`when`(stockService.getStockInfo(Mockito.any())).thenAnswer {
//            return@thenAnswer "***Nokia Corporation (NOK)***\n" +
//                    "\n" +
//                    "Цена 4.00 USD \uD83D\uDFE20.04 (1.01%)\n" +
//                    "50MA 4.03 USD\n" +
//                    "200MA 4.04 USD\n" +
//                    "Объём торгов 24 315 868\n" +
//                    "Средний объем 97 282 749"
//        }
//
//        val user = userService.findOrCreateUser(1)
//        val update = Update().apply { message = Message().apply { text = "nok?" } }
//        val response = messageHandler.handleMessage(user, update)
//        assertEquals(
//                "***Nokia Corporation (NOK)***\n" +
//                        "\n" +
//                        "Цена 4.00 USD \uD83D\uDFE20.04 (1.01%)\n" +
//                        "50MA 4.03 USD\n" +
//                        "200MA 4.04 USD\n" +
//                        "Объём торгов 24 315 868\n" +
//                        "Средний объем 97 282 749",
//                (response[0] as SendMessage).text
//        )
    }
}