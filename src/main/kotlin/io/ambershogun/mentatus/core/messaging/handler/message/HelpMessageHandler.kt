package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class HelpMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = ".*Справка\$"

    override fun handleMessage(user: User, update: Update): List<Validable> {
        return listOf(messageService.createSendMessage(user.chatId.toString(), "help"))
    }
}