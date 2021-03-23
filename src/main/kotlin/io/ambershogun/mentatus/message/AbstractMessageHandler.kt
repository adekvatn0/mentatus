package io.ambershogun.mentatus.message

import io.ambershogun.mentatus.user.User
import io.ambershogun.mentatus.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime
import java.util.*

abstract class AbstractMessageHandler : MessageHandler {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var messageSource: MessageSource

    final override fun handleMessage(chatId: Long, languageCode: String, inputMessage: String): List<SendMessage> {
        val user = userService.findOrCreateUser(chatId, languageCode)

        user.lastActive = LocalDateTime.now()
        userService.saveUser(user)

        return handleMessageInternal(user, inputMessage)
    }

    protected fun createMessage(user: User, messageName: String, vararg placeholders: Any): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = user.chatId.toString()
            this.text = messageSource.getMessage(
                    messageName,
                    placeholders,
                    Locale.forLanguageTag(user.locale)
            )
        }
    }

    protected abstract fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage>
}