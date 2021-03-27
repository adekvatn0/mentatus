package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.properties.AppProperties
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class HelpMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = ".*Справка\$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        return listOf(responseService.createSendMessage(user.chatId.toString(), "help"))
    }
}