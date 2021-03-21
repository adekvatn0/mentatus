package io.ambershogun.mentatus

import io.ambershogun.mentatus.core.properties.AppProperties
import io.ambershogun.mentatus.message.MessageHandlerRegistry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
final class MentatusBot(
        private val appProperties: AppProperties,
        private val registry: MessageHandlerRegistry
) : TelegramLongPollingBot() {

    private val logger = LoggerFactory.getLogger("messaging")

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun onUpdateReceived(update: Update) {
        val chatId = update.message.chatId
        val languageCode = update.message.from.languageCode
        val inputMessage = update.message.text

        try {
            val handler = registry.getHandler(inputMessage)

            val responseMessage = handler.handleMessage(chatId, languageCode, inputMessage)

            execute(responseMessage)
        } catch (e: Exception) {
            logger.error("Error while handling message: chatId = $chatId, message = $inputMessage", e)
        }
    }

    fun sendMessageText(chatId: Long, text: String) {
        val message = SendMessage().apply {
            enableMarkdown(true)
            this.chatId = chatId.toString()
            this.text = text
        }
        execute(message)
    }

    override fun getBotUsername(): String {
        return appProperties.bot.name!!
    }

    override fun getBotToken(): String {
        return appProperties.bot.token!!
    }
}