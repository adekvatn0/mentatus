package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.messaging.handler.message.HelpMessageHandler
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

class HelpMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: HelpMessageHandler

    override fun `test message regex`() {
        assertTrue("\uD83D\uDD30 Помощь".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "help")
        kotlin.test.assertEquals(
                messageSource.getMessage("help", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )
    }
}