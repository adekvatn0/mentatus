package io.ambershogun.mentatus.core.message.handler

import io.ambershogun.mentatus.core.message.AbstractMessageHandler
import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class CleanMessageHandler(
        private val priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^clean$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage> {
        priceNotificationService.deleteAllByUser(user.chatId)

        return listOf(createMessage(user, "notification.clean"))
    }
}