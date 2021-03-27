package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class CleanMessageHandler(
        private val priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^clean$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        priceNotificationService.deleteAllByUser(user.chatId)

        return listOf(responseService.createSendMessage(user.chatId.toString(), "notification.clean"))
    }
}