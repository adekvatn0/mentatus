package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import io.ambershogun.mentatus.core.entity.user.service.UserService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class StockInfoCallbackHandler(
        private val userService: UserService,
        private val stockService: StockService
) : AbstractCallbackHandler() {

    override fun messageRegEx(): String {
        return "^(\\/favorite/details).*"
    }

    override fun handleCallbackInternal(chatId: Long, callbackQueryId: String, messageId: Int, params: Map<String, String>): List<Validable> {
        val ticker = params["ticker"] ?: return emptyList()

        val user = userService.findOrCreateUser(chatId)

        val stock = stockService.getStock(ticker) ?: return emptyList()

        return listOf(
                SendMessage().apply {
                    this.chatId = chatId.toString()
                    enableMarkdown(true)
                    this.text = stockService.getStockInfo(stock)
                }
        )
    }
}