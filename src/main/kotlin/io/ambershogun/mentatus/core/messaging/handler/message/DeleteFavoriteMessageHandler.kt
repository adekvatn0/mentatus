package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable

@Component
class DeleteFavoriteMessageHandler : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\-(\\s+|)\\w+\$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        val ticker = getTicker(inputMessage)

        user.favoriteTickers.remove(ticker)
        userService.saveUser(user)

        return listOf(responseService.createSendMessage(
                user.chatId.toString(),
                "favorite.deleted"
        ))
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").drop(1)
    }
}