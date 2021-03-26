package io.ambershogun.mentatus.core.messaging.util

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Service
class ResponseService(
        private val messageSource: MessageSource
) {

    fun createSendMessage(chatId: String, messageName: String, vararg placeholders: Any): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = chatId
            this.text = messageSource.getMessage(
                    messageName,
                    placeholders,
                    Locale.forLanguageTag("ru")
            )
        }
    }
}