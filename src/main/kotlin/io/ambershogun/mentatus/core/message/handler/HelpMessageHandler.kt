package io.ambershogun.mentatus.core.message.handler

import io.ambershogun.mentatus.core.message.AbstractMessageHandler
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class HelpMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^help$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage> {
        return listOf(createMessage(user, "help"))
    }
}