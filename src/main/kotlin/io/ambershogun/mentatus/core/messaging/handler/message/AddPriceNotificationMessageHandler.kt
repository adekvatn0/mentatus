package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.exception.StockNotFoundException
import io.ambershogun.mentatus.core.entity.notification.price.service.PriceNotificationService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class AddPriceNotificationMessageHandler(
        private var priceNotificationService: PriceNotificationService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\w+(\\s+|)(<|>)(\\s+|)\\d+(.|,|)(\\d+|)"

    override fun handleMessage(user: User, update: Update): List<Validable> {
        return try {
            val priceNotification = priceNotificationService.createNotification(user.chatId, update.message.text)

            listOf(
                    responseService.createSendMessage(
                            user.chatId.toString(),
                            "notification.add",
                            priceNotification.toString()
                    )
            )
        } catch (e: StockNotFoundException) {
            listOf(
                    responseService.createSendMessage(
                            user.chatId.toString(),
                            "stock.not.found"
                    )
            )
        }
    }
}