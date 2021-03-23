package io.ambershogun.mentatus.core.message

import io.ambershogun.mentatus.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import kotlin.test.assertNotNull

class MessageHandlerRegistryTest : AbstractTest() {

    @Autowired
    lateinit var messageHandlerRegistry: MessageHandlerRegistry

    @Test
    fun `test registry`() {
        messageHandlerRegistry.register(object : MessageHandler {
            override fun messageRegEx(): String {
                return "^test$"
            }

            override fun handleMessage(chatId: Long, languageCode: String, inputMessage: String): List<SendMessage> {
                return listOf(SendMessage())
            }
        })

        assertNotNull(messageHandlerRegistry.getHandler("test"))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `test unknown message`() {
        messageHandlerRegistry.getHandler("strange message")
    }
}