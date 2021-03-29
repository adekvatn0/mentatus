package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable

@Component
class AddFavoriteMessageHandler(
        private var stockService: StockService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\+(\\s+|)\\w+\$"

    override fun handleMessageInternal(user: User, inputMessage: String): List<Validable> {
        val ticker = getTicker(inputMessage)

        if (!stockService.isStockExistByTicker(ticker)) {
            return listOf(responseService.createSendMessage(
                    user.chatId.toString(),
                    "stock.not.found"
            ))
        }

        user.favoriteTickers.add(ticker)
        userService.saveUser(user)

        return listOf(responseService.createSendMessage(
                user.chatId.toString(),
                "favorite.added"
        ))
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").drop(1)
    }
}