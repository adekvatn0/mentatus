package io.ambershogun.mentatus.core.messaging.util

import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.marketmaps.FinvizImageService
import io.ambershogun.mentatus.core.notification.market.Exchange
import io.ambershogun.mentatus.core.notification.market.Index
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
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
        private val stockService: StockService,
        private val finvizImageService: FinvizImageService
) {

    fun createMarketImagesMessage(): SendMediaGroup {
        val holder = finvizImageService.getHolder()

        val sectorsMedia = InputMediaPhoto()
        sectorsMedia.media = holder.sectorUrl

        val regionsMedia = InputMediaPhoto()
        regionsMedia.media = holder.regionsUrl

        return SendMediaGroup().apply {
            medias = listOf(sectorsMedia, regionsMedia)
        }
    }

    fun createMarketOverviewMessage(): SendMessage {
        val builder = StringBuilder()

        run {
            val indexTickers = Index.values().map {
                it.ticker
            }.toTypedArray()

            val stocks = stockService.getStocks(indexTickers)

            builder.append(messageSource.getMessage("market.indexes", emptyArray(), Locale.forLanguageTag("ru")))
            builder.append("\n")

            stocks.forEach {
                val prettyName = Index.findByTicker(it.key)!!.prettyName

                val quote = it.value.quote

                val percentChange = BigDecimal.valueOf(
                        (quote.price.toDouble() - quote.previousClose.toDouble()) / quote.previousClose.toDouble() * 100
                ).setScale(3, RoundingMode.HALF_DOWN)

                val redOrGreenEmoji = if (percentChange < BigDecimal.ZERO) {
                    "\uD83D\uDD34"
                } else {
                    "\uD83D\uDFE2"
                }

                val percentWithSign = if (percentChange > BigDecimal.ZERO) {
                    "+$percentChange"
                } else {
                    percentChange
                }

                builder.append(
                        messageSource.getMessage(
                                "market.element.index",
                                arrayOf(redOrGreenEmoji, prettyName, percentWithSign),
                                Locale.forLanguageTag("ru")
                        )
                )

                builder.append("\n")
            }
        }

        run {
            val exchangeTickers = Exchange.values().map {
                it.ticker
            }.toTypedArray()

            val stocks = stockService.getStocks(exchangeTickers)

            builder.append("\n")
            builder.append(messageSource.getMessage("market.exchanges", emptyArray(), Locale.forLanguageTag("ru")))
            builder.append("\n")

            stocks.forEach {
                val prettyName = Exchange.findByTicker(it.key)!!.prettyName

                val quote = it.value.quote

                val percentChange = BigDecimal.valueOf((
                        quote.price.toDouble() - quote.previousClose.toDouble()) / quote.previousClose.toDouble() * 100
                ).setScale(3, RoundingMode.HALF_DOWN)

                val redOrGreenEmoji = if (percentChange < BigDecimal.ZERO) {
                    "\uD83D\uDD34"
                } else {
                    "\uD83D\uDFE2"
                }

                val percentWithSign = if (percentChange > BigDecimal.ZERO) {
                    "+$percentChange"
                } else {
                    percentChange
                }

                builder.append(
                        messageSource.getMessage(
                                "market.element.exchange",
                                arrayOf(redOrGreenEmoji, prettyName, quote.price.setScale(2, RoundingMode.HALF_DOWN), percentWithSign),
                                Locale.forLanguageTag("ru")
                        )
                )

                builder.append("\n")
            }
        }

        return SendMessage().apply {
            enableMarkdown(true)
            this.text = builder.toString()
        }
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