package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage

@Component
class DeleteFavoriteCallbackHandler(
        private val userService: UserService
) : AbstractCallbackHandler() {

    private val logger = LoggerFactory.getLogger("messaging")

    override fun messageRegEx(): String {
        return "^(\\/favorite/delete).*"
    }

    override fun handleCallbackInternal(chatId: Long, callbackQueryId: String, messageId: Int, params: Map<String, String>): List<Validable> {
        val ticker = params["ticker"] ?: return emptyList()

        val user = userService.findOrCreateUser(chatId)

        user.favoriteTickers.remove(ticker)
        userService.saveUser(user)

        return listOf(
                AnswerCallbackQuery().apply {
                    this.text = "${ticker.toUpperCase()} удален из избранных"
                    this.callbackQueryId = callbackQueryId
                },
                DeleteMessage().apply {
                    this.chatId = chatId.toString()
                    this.messageId = messageId
                }
        )
    }
}