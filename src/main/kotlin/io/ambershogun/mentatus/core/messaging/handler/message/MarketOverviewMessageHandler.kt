package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class MarketOverviewMessageHandler : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "\uD83D\uDDFA Рынки"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val marketOverviewMessage = messageService.createMarketOverviewMessage()
        marketOverviewMessage.chatId = user.chatId.toString()

        val marketImagesMessage = messageService.createMarketImagesMessage()
        marketImagesMessage.chatId = user.chatId.toString()

        return listOf(marketOverviewMessage, marketImagesMessage)
    }
}