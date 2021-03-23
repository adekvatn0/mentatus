package io.ambershogun.mentatus.core.message.handler

import io.ambershogun.mentatus.core.message.AbstractMessageHandler
import io.ambershogun.mentatus.core.message.util.KeyboardCreator
import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class ListNotificationsMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = ".*уведомления$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage> {
        val priceNotifications = priceNotificationService.findAllByUser(user.chatId)
        if (priceNotifications.isEmpty()) {
            return listOf(createMessage(user, "notification.list.empty"))
        }

        val messages = mutableListOf<SendMessage>()

        priceNotifications.forEach {
            val message = createMessage(user, "notification.list", it)
            message.replyMarkup = KeyboardCreator.createNotificationDeleteButton(it.id)
            messages.add(message)
        }

        return messages
    }
}