package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.notification.price.threshold.service.PriceThresholdService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class ListPriceThresholdsMessageHandler(
        private var priceThresholdService: PriceThresholdService
) : AbstractMessageHandler() {
    override fun messageRegEx() = ".*Уведомления$"

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val priceNotifications = priceThresholdService.findAllByUser(user.chatId)
        if (priceNotifications.isEmpty()) {
            val message = messageService.createSendMessage(
                    user.chatId.toString(),
                    "notification.list.empty"
            )
            return listOf(message)
        }

        val messages = mutableListOf<SendMessage>()

        priceNotifications.forEach {
            val message = messageService.createSendMessage(
                    user.chatId.toString(),
                    "notification.list",
                    it
            ).apply {
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
                            callbackData = "/notification/delete?id=$notificationId"
                        })
                )
        )
    }
}