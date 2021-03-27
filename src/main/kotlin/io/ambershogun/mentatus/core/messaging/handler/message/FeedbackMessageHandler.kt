package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.properties.AppProperties
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class FeedbackMessageHandler(
        private val appProperties: AppProperties
) : AbstractMessageHandler() {
    override fun messageRegEx(): String {
        return ".*Обратная связь$"
    }

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        val message = responseService.createSendMessage(
                user.chatId.toString(),
                "feedback"
        )

        message.enableMarkdown(false)

        return listOf(message)
    }
}