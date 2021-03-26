package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class RetryLastMessageHandler(
        private val handlerRegistry: HandlerRegistry
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return ".*Повторить$"
    }

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        val lastRetryableAction = user.lastRetryableAction ?: return emptyList()

        return handlerRegistry.getMessageHandler(lastRetryableAction).handleMessage(user.chatId, lastRetryableAction)
    }
}