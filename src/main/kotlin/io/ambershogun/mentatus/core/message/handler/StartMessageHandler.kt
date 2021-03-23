package io.ambershogun.mentatus.core.message.handler

import io.ambershogun.mentatus.core.message.AbstractMessageHandler
import io.ambershogun.mentatus.core.message.util.KeyboardCreator
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class StartMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\/start$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage> {
        val message = createMessage(user, "start")
        message.replyMarkup = KeyboardCreator.createReplyKeyboard(
                arrayOf(
                        arrayOf("\uD83D\uDD14 Уведомления"),
                        arrayOf("\uD83D\uDD30 Помощь"),
                        arrayOf("\uD83D\uDCEE Обратная связь")
                )
        )
        return listOf(message)
    }
}