package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.handler.message.FavoriteMessageHandler
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FavoritMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var handler: FavoriteMessageHandler



    override fun `test message regex`() {
        assertTrue("❤️ Избранные".matches(Regex(handler.messageRegEx())))
        assertFalse("qwe".matches(Regex(handler.messageRegEx())))
    }

    override fun `test handle message`() {
        val update = Update().apply { message = Message().apply { text = "❤️ Избранные" } }
        val response = handler.handleMessage(User(1), update)
        assertEquals(
                messageSource.getMessage("favorite.empty", emptyArray(), Locale.forLanguageTag("ru")),
                (response[0] as SendMessage).text
        )
    }
}