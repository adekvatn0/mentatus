package io.ambershogun.mentatus.core

import io.ambershogun.mentatus.core.properties.AppProperties
import io.ambershogun.mentatus.core.message.MessageHandlerRegistry
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
        when (getInputMessageType(update)) {
            MessageType.MESSAGE -> handleMessage(update)
            MessageType.CALLBACK -> handleCallback(update)
        }
    }

    private fun getInputMessageType(update: Update): MessageType {
        if (update.message != null) {
            return MessageType.MESSAGE
        }
        if (update.callbackQuery != null) {
            return MessageType.CALLBACK
        }

        throw IllegalStateException("Can't determine message type")
    }

    private fun handleMessage(update: Update) {
        val chatId = update.message.chatId
        val languageCode = update.message.from.languageCode
        val inputMessage = update.message.text

        try {
            val handler = registry.getHandler(inputMessage)

            val messages = handler.handleMessage(chatId, languageCode, inputMessage)
            messages.forEach(this::execute)
        } catch (e: Exception) {
            logger.error("Error while handling message: chatId = $chatId, message = $inputMessage", e)
        }
    }

    private fun handleCallback(update: Update) {
        print("Not yet implemented")
        TODO("Not yet implemented")
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

    private enum class MessageType { MESSAGE, CALLBACK }
}