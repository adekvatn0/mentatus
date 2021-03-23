package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime
import java.util.*

abstract class AbstractMessageHandler : MessageHandler {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var messageSource: MessageSource

    final override fun handleMessage(chatId: Long, inputMessage: String): List<BotApiMethod<Message>> {
        val user = userService.findOrCreateUser(chatId)

        user.lastActive = LocalDateTime.now()
        userService.saveUser(user)

        return handleMessageInternal(user, inputMessage)
    }

    protected fun createSendMessage(user: User, messageName: String, vararg placeholders: Any): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = user.chatId.toString()
            this.text = messageSource.getMessage(
                    messageName,
                    placeholders,
                    Locale.forLanguageTag("ru")
            )
        }
    }

    protected abstract fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>>
}