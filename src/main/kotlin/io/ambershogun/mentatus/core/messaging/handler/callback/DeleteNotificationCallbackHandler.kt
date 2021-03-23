package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.notification.price.repo.PriceNotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage

@Component
class DeleteNotificationCallbackHandler(
        private val priceNotificationRepository: PriceNotificationRepository
) : AbstractCallbackHandler() {

    private val logger = LoggerFactory.getLogger("messaging")

    override fun dataRegEx(): String {
        return "^(\\/delete).*"
    }

    override fun handleCallbackInternal(chatId: Long, callbackQueryId: String, messageId: Int, params: Map<String, String>): List<BotApiMethod<Boolean>> {
        val notificationId = params["notificationId"]
        if (notificationId == null) {
            logger.error("Required param not found, message ignored\nchatId=$chatId\ndata=$params")
            return emptyList()
        }

        priceNotificationRepository.deleteById(notificationId)

        return listOf(
                AnswerCallbackQuery().apply {
                    this.text = "Уведомление удалено"
                    this.callbackQueryId = callbackQueryId
                },
                DeleteMessage().apply {
                    this.chatId = chatId.toString()
                    this.messageId = messageId
                }
        )
    }
}