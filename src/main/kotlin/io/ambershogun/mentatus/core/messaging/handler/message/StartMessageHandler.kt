package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.util.KeyboardCreator
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class StartMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\/start$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        val message = responseService.createSendMessage(user.chatId.toString(), "start")
        message.replyMarkup = KeyboardCreator.createReplyKeyboard(
                arrayOf(
                        arrayOf("\uD83D\uDD14 Уведомления", "🗺 Рынки"),
                        arrayOf("\uD83E\uDDF0 Настройки", "⏮ Повторить"),
                        arrayOf("\uD83D\uDD30 Справка", "\uD83D\uDCEE Обратная связь")
                )
        )
        return listOf(message)
    }
}