package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class ListNotificationsMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = ".*уведомления$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        val priceNotifications = priceNotificationService.findAllByUser(user.chatId)
        if (priceNotifications.isEmpty()) {
            return listOf(createSendMessage(user, "notification.list.empty"))
        }

        val messages = mutableListOf<SendMessage>()

        priceNotifications.forEach {
            val message = createSendMessage(user, "notification.list", it).apply {
                replyMarkup = createDeleteButton(it.id!!)
            }
            messages.add(message)
        }

        return messages
    }

    private fun createDeleteButton(notificationId: String): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
                listOf(
                        listOf(InlineKeyboardButton().apply {
                            text = "\uD83D\uDD15 Удалить"
                            callbackData = "/delete?notificationId=$notificationId"
                        })
                )
        )
    }
}