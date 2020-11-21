package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.message.AbstractMessageHandler
import io.ambershogun.mentatus.notification.price.PriceNotificationService
import io.ambershogun.mentatus.notification.price.exception.StockNotFoundException
import io.ambershogun.mentatus.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class AddPriceNotificationMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "[a-zA-Z].*( )*(<|>)( )*[0-9].*(.|,)*[0-9].*"

    override fun handleMessageInternal(user: User, inputMessage: String): SendMessage {
        return try {
            val priceNotification = priceNotificationService.createNotification(user.chatId, inputMessage)
            createMessage(user, "notification.add", priceNotification.ticker, priceNotification.price)
        } catch (e: StockNotFoundException) {
            createMessage(user, "notification.add.stock.not.found")
        }
    }

    companion object {
        const val LESS_DELIMITER = "<"
        const val GREATER_DELIMITER = ">"
    }
}