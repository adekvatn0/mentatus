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

        val stock = stockService.getStock(ticker)
                ?: return listOf(responseService.createSendMessage(
                        user.chatId.toString(),
                        "stock.not.found"
                ))

        val isAdded = user.favoriteTickers.add(stock.symbol)
        userService.saveUser(user)

        return if (isAdded) {
            listOf(responseService.createSendMessage(
                    user.chatId.toString(),
                    "favorite.added"
            ))
        } else {
            listOf(responseService.createSendMessage(
                    user.chatId.toString(),
                    "favorite.already.added"
            ))
        }
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").drop(1)
    }
}