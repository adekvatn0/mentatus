package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.notification.indexes.Index
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class MarketMessageHandler(
        @Value("\${marketmaps.dir}") private val marketMapsDir: String,
        private val stockService: StockService
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "\uD83D\uDDFA Рынки"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
//        val sectorsMedia = InputMediaPhoto()
//        sectorsMedia.setMedia(File("$marketMapsDir/sectors.png"), "sectors.png")
//
//        val regionsMedia = InputMediaPhoto()
//        regionsMedia.setMedia(File("$marketMapsDir/sectors.png"), "regions.png")
//
//        val sendMediaGroup = SendMediaGroup()
//        sendMediaGroup.chatId = user.chatId.toString()
//        sendMediaGroup.medias = listOf(
//                sectorsMedia, regionsMedia
//        )

        val indexTickers = Index.values().map {
            it.ticker
        }.toTypedArray()

        val stocks = stockService.getStocks(indexTickers)

        val text = messageService.createIndexesText(stocks)

        return listOf(SendMessage().apply {
            this.chatId = user.chatId.toString()
            this.enableMarkdown(true)
            this.text = text
        })
    }
}