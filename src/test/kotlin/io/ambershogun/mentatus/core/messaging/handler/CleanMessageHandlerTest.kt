package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.messaging.handler.message.CleanMessageHandler
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

class CleanMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: CleanMessageHandler

    override fun `test message regex`() {
        assertTrue("clean".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "clean")
        kotlin.test.assertEquals(
                messageSource.getMessage("notification.clean", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )
    }

}