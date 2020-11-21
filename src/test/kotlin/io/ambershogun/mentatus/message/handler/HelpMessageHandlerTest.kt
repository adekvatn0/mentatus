package io.ambershogun.mentatus.message.handler

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*

class HelpMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: HelpMessageHandler

    override fun `test message regex`() {
        assertTrue("help".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "ru", "help")
        kotlin.test.assertEquals(
                messageSource.getMessage("help", emptyArray(), Locale.forLanguageTag("ru")),
                response.text
        )
    }
}