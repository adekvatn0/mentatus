package io.ambershogun.mentatus.core.messaging

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.messaging.handler.message.MessageHandler
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import kotlin.test.assertNotNull

class HandlerRegistryTest : AbstractTest() {

    @Autowired
    lateinit var handlerRegistry: HandlerRegistry

    @Test
    fun `test registry`() {
        handlerRegistry.register(object : MessageHandler {
            override fun messageRegEx(): String {
                return "^test$"
            }

            override fun handleMessage(chatId: Long, inputMessage: String): List<SendMessage> {
                return listOf(SendMessage())
            }
        })

        assertNotNull(handlerRegistry.getMessageHandler("test"))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `test unknown message`() {
        handlerRegistry.getMessageHandler("strange message")
    }
}