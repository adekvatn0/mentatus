package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class StockInfoMessageHandler(
        private val stockService: StockService
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "^\\w+(\\s+|)\\?$"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val ticker = getTicker(update.message.text)

        val stock = stockService.getStock(ticker) ?: return listOf(
                responseService.createSendMessage(user.chatId.toString(), "stock.not.found")
        )


        val sendMessage = SendMessage().apply {
            enableMarkdown(true)
            this.chatId = user.chatId.toString()
            this.text = stockService.getStockInfo(stock)
        }

        return listOf(sendMessage)
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").dropLast(1)
    }
}