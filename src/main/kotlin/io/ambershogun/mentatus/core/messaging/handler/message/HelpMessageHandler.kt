package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class HelpMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = ".*Справка\$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        return listOf(responseService.createSendMessage(user.chatId.toString(), "help"))
    }
}