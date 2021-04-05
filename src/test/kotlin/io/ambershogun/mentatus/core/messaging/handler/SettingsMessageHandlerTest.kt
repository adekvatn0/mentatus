package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.handler.message.SettingsMessageHandler
import io.ambershogun.mentatus.core.messaging.util.ResponseService
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var handler: SettingsMessageHandler

    @Autowired
    lateinit var responseService: ResponseService

    @Autowired
    lateinit var userService: UserService

    override fun `test message regex`() {
        assertTrue("⚙ Настройки".matches(Regex(handler.messageRegEx())))
        assertFalse("123".matches(Regex(handler.messageRegEx())))
    }

    override fun `test handle message`() {
        val user = userService.findOrCreateUser(1)

        val update = Update().apply { message = Message().apply { text = "⚙ Настройки" } }
        val response = handler.handleMessage(User(1), update)
        assertEquals(
                messageSource.getMessage("settings", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )

        val replyMarkup = responseService.createSettingsButtons(user.settings)

        assertEquals(
                replyMarkup,
                (response[0] as SendMessage).replyMarkup
        )
    }
}