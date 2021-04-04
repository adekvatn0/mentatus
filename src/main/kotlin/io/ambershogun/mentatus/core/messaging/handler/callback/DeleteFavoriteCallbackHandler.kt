package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class DeleteFavoriteCallbackHandler : AbstractCallbackHandler() {

    override fun messageRegEx(): String {
        return "^(\\/favorite/delete).*"
    }

    override fun handleCallbackInternal(user: User, update: Update, params: Map<String, String>): List<Validable> {
        val ticker = params["ticker"] ?: return emptyList()

        user.favoriteTickers.remove(ticker)
        userService.saveUser(user)

        return listOf(
                responseService.createPushMessage(
                        user.chatId.toString(),
                        update.callbackQuery.id,
                        "favorite.deleted",
                        ticker.toUpperCase()
                ),
                responseService.createDeleteMessage(
                        user.chatId.toString(),
                        update.callbackQuery.message.messageId
                )
        )
    }
}