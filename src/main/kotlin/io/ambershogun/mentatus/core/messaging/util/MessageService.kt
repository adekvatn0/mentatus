package io.ambershogun.mentatus.core.messaging.util

import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.notification.indexes.Index
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import yahoofinance.Stock
import yahoofinance.YahooFinance
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Service
class MessageService(
        private val messageSource: MessageSource,
) {

    fun createIndexesText(stocks: MutableMap<String, Stock>): String {
        val builder = StringBuilder()

        builder.append(messageSource.getMessage("indexes", emptyArray(), Locale.forLanguageTag("ru")))
        builder.append("\n")
        builder.append(" ")
        builder.append("\n")

        stocks.forEach {
            val prettyName = Index.findByTicker(it.key)

            val quote = it.value.quote

            val percentChange = (quote.price.toDouble() - quote.previousClose.toDouble()) / quote.previousClose.toDouble() * 100

            val redOrGreenEmoji = if (percentChange < 0) {
                "\uD83D\uDD34"
            } else {
                "\uD83D\uDFE2"
            }

            builder.append(
                    messageSource.getMessage(
                            "indexes.element",
                            arrayOf(redOrGreenEmoji, prettyName, percentChange),
                            Locale.forLanguageTag("ru")
                    )
            )

            builder.append("\n")
        }

        return builder.toString()
    }

    fun createStockInfoMessage(chatId: String, stock: Stock): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = chatId
            this.text = messageSource.getMessage(
                    "stock.info",
                    arrayOf(
                            "${stock.name} (${stock.symbol})",
                            getPriceInfo(stock),
                            "${stock.quote.priceAvg50.setScale(2, RoundingMode.HALF_DOWN)} ${stock.currency}",
                            "${stock.quote.priceAvg200.setScale(2, RoundingMode.HALF_DOWN)} ${stock.currency}",
                            BigDecimal.valueOf(stock.quote.volume).setScale(2, RoundingMode.HALF_DOWN),
                            BigDecimal.valueOf(stock.quote.avgVolume).setScale(2, RoundingMode.HALF_DOWN)
                    ),
                    Locale.forLanguageTag("ru")
            )
            this.replyMarkup = createOpenOnYahooFinanceButton(stock.symbol)
        }
    }

    private fun createOpenOnYahooFinanceButton(ticker: String): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
                listOf(
                        listOf(
                                InlineKeyboardButton().apply {
                                    text = messageSource.getMessage(
                                            "favorite.yahoo.button",
                                            emptyArray(),
                                            Locale.forLanguageTag("ru")
                                    )
                                    url = "https://finance.yahoo.com/quote/$ticker"
                                }
                        )
                )
        )

    }

    fun getStockShortInfo(ticker: String): String {
        val stock = YahooFinance.get(ticker)

        val builder = StringBuilder()

        builder.append(stock.symbol)
        builder.append(" ")
        builder.append(getPriceInfo(stock))
        return builder.toString()
    }

    fun getPriceInfo(stock: Stock): String {
        val quote = stock.quote

        val builder = StringBuilder()

        builder.append(quote.price.setScale(2, RoundingMode.HALF_DOWN))
        builder.append(" ")
        builder.append(stock.currency)
        builder.append(" ")

        val priceChange = quote.price - quote.previousClose
        val percentChange = (quote.price.toDouble() - quote.previousClose.toDouble()) / quote.previousClose.toDouble() * 100
        if (percentChange < 0) {
            builder.append("\uD83D\uDD34")
        } else {
            builder.append("\uD83D\uDFE2")
        }

        builder.append(priceChange)
        builder.append(" ")
        builder.append("(${BigDecimal.valueOf(percentChange).setScale(2, RoundingMode.HALF_DOWN)}%)")

        return builder.toString()
    }

    fun createDeleteMessage(chatId: String, messageId: Int): DeleteMessage {
        return DeleteMessage().apply {
            this.chatId = chatId
            this.messageId = messageId
        }
    }

    fun createPushMessage(chatId: String, callbackQueryId: String, messageName: String, vararg placeholders: Any): AnswerCallbackQuery {
        return AnswerCallbackQuery().apply {
            this.text = messageSource.getMessage(
                    messageName,
                    placeholders,
                    Locale.forLanguageTag("ru")
            )
            this.callbackQueryId = callbackQueryId
        }
    }

    fun createSendMessage(chatId: String, messageName: String, vararg placeholders: Any): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = chatId
            this.text = messageSource.getMessage(
                    messageName,
                    placeholders,
                    Locale.forLanguageTag("ru")
            )
        }
    }

    fun createSettingsMessage(user: User): SendMessage {
        return SendMessage().apply {
            enableMarkdown(true)
            this.chatId = user.chatId.toString()
            this.text = messageSource.getMessage(
                    "settings",
                    emptyArray(),
                    Locale.forLanguageTag("ru")
            ).apply {
                replyMarkup = createSettingsButtons(user.settings)
            }
        }
    }

    fun createSettingsButtons(settings: Map<Setting, Boolean>): InlineKeyboardMarkup {
        val settingButtons = mutableListOf<List<InlineKeyboardButton>>()
        for (setting in settings) {
            val settingPrettyName = messageSource.getMessage(
                    setting.key.messageName,
                    emptyArray(),
                    Locale.forLanguageTag("ru")
            )

            val settingBallotEmoji = if (setting.value) {
                CHECKED_BALLOT_EMOJI
            } else {
                UNCHECKED_BALLOT_EMOJI
            }

            val settingWithValueText = messageSource.getMessage(
                    "setting.template",
                    arrayOf(settingBallotEmoji, settingPrettyName),
                    Locale.forLanguageTag("ru")
            )

            settingButtons.add(
                    listOf(
                            InlineKeyboardButton().apply {
                                text = settingWithValueText
                                callbackData = "/settings?name=${setting}&value=${!setting.value}"
                            }
                    )
            )
        }
        return InlineKeyboardMarkup(settingButtons)
    }

    companion object {
        const val CHECKED_BALLOT_EMOJI = "✅"
        const val UNCHECKED_BALLOT_EMOJI = "❎"
    }
}