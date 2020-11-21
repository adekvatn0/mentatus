package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.notification.price.PriceNotificationService
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class CleanMessageHandler(
        private val priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^clean$"

    override fun handleMessageInternal(user: User, inputMessage: String): SendMessage {
        priceNotificationService.deleteAllByUser(user.chatId)

        return createMessage(user, "notification.clean")
    }
}