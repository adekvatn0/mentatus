package io.ambershogun.mentatus.core.messaging

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.handler.MessageHandler
import io.ambershogun.mentatus.core.util.MessageType
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
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

            override fun handleMessage(user: User, update: Update): List<Validable> {
                return listOf(SendMessage())
            }
        })

        val update = Update().apply { message = Message().apply { text = "test" } }

        assertNotNull(handlerRegistry.getHandler(MessageType.MESSAGE, update))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `test unknown message`() {
        val update = Update().apply { message = Message().apply { text = "strange message" } }
        handlerRegistry.getHandler(MessageType.MESSAGE, update)
    }
}