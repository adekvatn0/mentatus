package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class CleanMessageHandler(
        private val priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^clean$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        priceNotificationService.deleteAllByUser(user.chatId)

        return listOf(createSendMessage(user, "notification.clean"))
    }
}