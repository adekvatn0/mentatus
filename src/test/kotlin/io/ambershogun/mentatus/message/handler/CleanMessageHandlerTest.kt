package io.ambershogun.mentatus.message.handler

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*

class CleanMessageHandlerTest: AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: CleanMessageHandler

    override fun `test message regex`() {
        assertTrue("clean".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val response = messageHandler.handleMessage(1, "ru", "clean")
        kotlin.test.assertEquals(
                messageSource.getMessage("notification.clean", emptyArray(), Locale.forLanguageTag("ru")),
                response.text
        )
    }

}