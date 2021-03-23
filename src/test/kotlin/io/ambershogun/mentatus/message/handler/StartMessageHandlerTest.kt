package io.ambershogun.mentatus.message.handler

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.test.assertEquals

class StartMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: StartMessageHandler

    override fun `test message regex`() {
        assertTrue("/start".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "ru", "/start")
        assertEquals(
                messageSource.getMessage("start", emptyArray(), Locale.forLanguageTag("ru")),
                response[0].text
        )
    }

}