package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class AddFavoriteMessageHandler(
        private var stockService: StockService
) : AbstractMessageHandler() {
    override fun messageRegEx() = "^\\+(\\s+|)\\w+\$"

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val ticker = getTicker(update.message.text)

        val stock = stockService.getStock(ticker)
                ?: return listOf(messageService.createSendMessage(
                        user.chatId.toString(),
                        "stock.not.found"
                ))

        val isAdded = user.favoriteTickers.add(stock.symbol)
        userService.saveUser(user)

        return if (isAdded) {
            listOf(messageService.createSendMessage(
                    user.chatId.toString(),
                    "favorite.added"
            ))
        } else {
            listOf(messageService.createSendMessage(
                    user.chatId.toString(),
                    "favorite.already.added"
            ))
        }
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").drop(1)
    }
}