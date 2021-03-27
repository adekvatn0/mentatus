package io.ambershogun.mentatus.core

import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import io.ambershogun.mentatus.core.messaging.util.ResponseService
import io.ambershogun.mentatus.core.properties.AppProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
final class MentatusBot(
        private val appProperties: AppProperties,
        private val registry: HandlerRegistry,
        private val responseService: ResponseService
) : TelegramLongPollingBot() {

    private val logger = LoggerFactory.getLogger("messaging")

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun onUpdateReceived(update: Update) {
        try {
            when (getInputMessageType(update)) {
                MessageType.MESSAGE -> handleMessage(update)
                MessageType.CALLBACK -> handleCallback(update)
            }
        } catch (e: UnsupportedOperationException) {
            val message = responseService.createSendMessage(
                    getChatId(update),
                    "message.not.supported"
            )

            execute(message)
        }
    }

    private fun getChatId(update: Update): String {
        return when (getInputMessageType(update)) {
            MessageType.MESSAGE -> update.message.chatId.toString()
            MessageType.CALLBACK -> update.callbackQuery.message.chat.id.toString()
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
        val inputMessage = update.message.text

        val responseMessages = registry.getMessageHandler(inputMessage)
                .handleMessage(chatId, inputMessage)

        responseMessages.forEach {
            when (it.javaClass) {
                SendMessage::class.java -> execute(it as SendMessage)
                DeleteMessage::class.java -> execute(it as DeleteMessage)
                SendMediaGroup::class.java -> execute(it as SendMediaGroup)
            }
        }
    }

    private fun handleCallback(update: Update) {
        val chatId = update.callbackQuery.message.chat.id
        val callbackQueryId = update.callbackQuery.id
        val messageId = update.callbackQuery.message.messageId
        val data = update.callbackQuery.data

        val responseMessage = registry.getCallbackHandler(data)
                .handleCallback(chatId, callbackQueryId, messageId, data)

        responseMessage.forEach(this::execute)
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