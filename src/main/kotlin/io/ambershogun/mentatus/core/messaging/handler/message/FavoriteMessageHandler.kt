package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class FavoriteMessageHandler(
        private val stockService: StockService
) : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return ".*Избранные\$"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
        val favoriteTickers = user.favoriteTickers

        if (CollectionUtils.isEmpty(favoriteTickers)) {
            return listOf(responseService.createSendMessage(
                    user.chatId.toString(),
                    "favorite.empty"
            ))
        }

        val responses = mutableListOf<Validable>()
        favoriteTickers.forEach {
            responses.add(
                    responseService.createSendMessage(
                            user.chatId.toString(),
                            "favorite.element",
                            responseService.getStockShortInfo(it)
                    ).apply {
                        replyMarkup = createInlineKeyboard(it)
                    }
            )
        }

        return responses
    }

    private fun createInlineKeyboard(ticker: String): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
                listOf(
                        listOf(
                                InlineKeyboardButton().apply {
                                    text = "ℹ️ Детали"
                                    callbackData = "/favorite/details?ticker=$ticker"
                                },
                                InlineKeyboardButton().apply {
                                    text = "\uD83D\uDDD1️ Удалить"
                                    callbackData = "/favorite/delete?ticker=$ticker"
                                }
                        )
                )
        )
    }
}