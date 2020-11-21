package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class HelpMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^help$"

    override fun handleMessageInternal(user: User, inputMessage: String): SendMessage {
        return createMessage(user, "help")
    }
}