package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.util.KeyboardCreator
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class StartMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\/start$"

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val message = responseService.createSendMessage(user.chatId.toString(), "start")
        message.replyMarkup = KeyboardCreator.createReplyKeyboard(
                arrayOf(
                        arrayOf("❤️ Избранные", "🗺 Рынки"),
                        arrayOf("\uD83D\uDD14 Уведомления", "⚙ Настройки"),
                        arrayOf("\uD83D\uDD30 Справка", "\uD83D\uDCEE Обратная связь")
                )
        )
        return listOf(message)
    }
}