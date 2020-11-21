package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.notification.price.PriceNotification
import io.ambershogun.mentatus.notification.price.PriceNotificationService
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.stream.Collectors

@Component
class ListNotificationsMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^list$"

    override fun handleMessageInternal(user: User, inputMessage: String): SendMessage {
        val priceNotifications = priceNotificationService.findAllByUser(user.chatId)
        if (priceNotifications.isEmpty()) {
            return createMessage(user, "notification.list.empty")
        }

        return createMessage(user, "notification.list", toList(priceNotifications))
    }

    private fun toList(priceNotifications: List<PriceNotification>): String {
        return priceNotifications.stream()
                .map { it.toString() }
                .collect(Collectors.joining("\n"))
    }
}