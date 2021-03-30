package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class FeedbackMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx(): String {
        return ".*Обратная связь$"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val message = responseService.createSendMessage(
                user.chatId.toString(),
                "feedback"
        )

        message.enableMarkdown(false)

        return listOf(message)
    }
}