package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.handler.message.FeedbackMessageHandler
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

class FeedbackMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: FeedbackMessageHandler

    override fun `test message regex`() {
        assertTrue("\uD83D\uDCEE Обратная связь".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        val update = Update().apply { message = Message().apply { text = "help" } }
        val response = messageHandler.handleMessage(User(1), update)
        kotlin.test.assertEquals(
                messageSource.getMessage("feedback", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )
    }
}