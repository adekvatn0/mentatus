package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.message.util.KeyboardCreator
import io.ambershogun.mentatus.notification.price.PriceNotification
import io.ambershogun.mentatus.notification.price.PriceNotificationService
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import java.util.stream.Collectors

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