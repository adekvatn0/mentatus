package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.notification.price.exception.StockNotFoundException
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class AddPriceNotificationMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\w+(\\s+|)(<|>)(\\s+|)\\d+(.|,|)(\\d+|)"

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        return try {
            val priceNotification = priceNotificationService.createNotification(user.chatId, inputMessage)

            listOf(
                    createSendMessage(
                            user,
                            "notification.add",
                            priceNotification.toString()
                    )
            )
        } catch (e: StockNotFoundException) {
            listOf(
                    createSendMessage(user, "notification.add.stock.not.found")
            )
        }
    }
}