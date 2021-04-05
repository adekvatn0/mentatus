package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class StockInfoCallbackHandler(
        private val stockService: StockService
) : AbstractCallbackHandler() {

    override fun messageRegEx(): String {
        return "^(\\/favorite/details).*"
    }

    override fun handleCallbackInternal(user: User, update: Update, params: Map<String, String>): List<Validable> {
        val ticker = params["ticker"] ?: return emptyList()

        val stock = stockService.getStock(ticker) ?: return emptyList()

        return listOf(
                SendMessage().apply {
                    this.chatId = user.chatId.toString()
                    enableMarkdown(true)
                    this.text = stockService.getStockInfo(stock)
                }
        )
    }
}