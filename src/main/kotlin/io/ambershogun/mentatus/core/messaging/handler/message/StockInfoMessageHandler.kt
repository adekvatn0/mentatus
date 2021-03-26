package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class StockInfoMessageHandler(
        private val stockService: StockService
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "^\\w+(\\s+|)\\?$"
    }

    override fun handleMessageInternal(user: User, inputMessage: String): List<BotApiMethod<Message>> {
        val stock = stockService.getStock(getTicker(inputMessage)) ?: return listOf(
                responseService.createSendMessage(user.chatId.toString(), "stock.not.found")
        )

        val sendMessage = responseService.createSendMessage(
                user.chatId.toString(),
                "stock.info",
                stock.quote.price,
                stock.quote.priceAvg50,
                stock.quote.priceAvg200,
                stock.quote.volume,
                stock.quote.avgVolume
        )

        return listOf(sendMessage)
    }

    private fun getTicker(inputMessage: String): String {
        return inputMessage.replace(" ", "").dropLast(1)
    }

}