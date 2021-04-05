package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.notification.price.threshold.repo.PriceThresholdRepository
import io.ambershogun.mentatus.core.entity.user.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class DeleteNotificationCallbackHandler(
        private val priceThresholdRepository: PriceThresholdRepository
) : AbstractCallbackHandler() {

    private val logger = LoggerFactory.getLogger("messaging")

    override fun messageRegEx(): String {
        return "^(\\/notification/delete).*"
    }

    override fun handleCallbackInternal(user: User, update: Update, params: Map<String, String>): List<Validable> {
        val notificationId = params["id"]
        if (notificationId == null) {
            logger.error("Required param not found, message ignored\nchatId=${user.chatId}\ndata=$params")
            return emptyList()
        }

        priceThresholdRepository.deleteById(notificationId)

        return listOf(
                responseService.createPushMessage(
                        user.chatId.toString(),
                        update.callbackQuery.id,
                        "notification.deleted"
                ),
                responseService.createDeleteMessage(
                        user.chatId.toString(),
                        update.callbackQuery.message.messageId
                )
        )
    }
}