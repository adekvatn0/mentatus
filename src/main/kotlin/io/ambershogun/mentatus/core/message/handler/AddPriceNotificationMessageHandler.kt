package io.ambershogun.mentatus.core.message.handler

import io.ambershogun.mentatus.core.message.AbstractMessageHandler
import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.notification.price.exception.StockNotFoundException
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class AddPriceNotificationMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\w+(\\s+|)(<|>)(\\s+|)\\d+(.|,|)(\\d+|)"

    override fun handleMessageInternal(user: User, inputMessage: String): List<SendMessage> {
        return try {
            val priceNotification = priceNotificationService.createNotification(user.chatId, inputMessage)

            listOf(
                    createMessage(
                            user,
                            "notification.add",
                            priceNotification.toString()
                    )
            )
        } catch (e: StockNotFoundException) {
            listOf(
                    createMessage(user, "notification.add.stock.not.found")
            )
        }
    }
}