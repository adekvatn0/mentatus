package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.notification.price.PriceNotificationService
import io.ambershogun.mentatus.notification.price.exception.StockNotFoundException
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.math.BigDecimal

@Component
class AddPriceNotificationMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\w+(\\s+|)(<|>)(\\s+|)\\d+(.|,|)(\\d+|)"

    override fun handleMessageInternal(user: User, inputMessage: String): SendMessage {
        return try {
            val priceNotification = priceNotificationService.createNotification(user.chatId, inputMessage)
            createMessage(
                    user,
                    "notification.add",
                    priceNotification.toString()
            )
        } catch (e: StockNotFoundException) {
            createMessage(user, "notification.add.stock.not.found")
        }
    }
}