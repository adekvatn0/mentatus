package io.ambershogun.mentatus.core

import io.ambershogun.mentatus.core.entity.user.PersonalData
import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import io.ambershogun.mentatus.core.messaging.util.MessageService
import io.ambershogun.mentatus.core.properties.AppProperties
import io.ambershogun.mentatus.core.util.MessageType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.time.LocalDateTime

@Component
class MentatusBot(
        private val appProperties: AppProperties,
        private val registry: HandlerRegistry,
        private val messageService: MessageService,
        private val userService: UserService
) : TelegramLongPollingBot() {

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    val logger = LoggerFactory.getLogger("messaging")

    override fun onUpdateReceived(update: Update) {
        val chatId = getChatId(update)

        try {
            val messageType = getInputMessageType(update)

            val user = userService.findOrCreateUser(chatId)
            user.lastActive = LocalDateTime.now()
            user.personalData = getPersonalData(update)
            updateSettings(user)
            userService.saveUser(user)

            val responseMessages = registry.getHandler(messageType, update).handleMessage(user, update)

            sendMessages(responseMessages)
        } catch (e: UnsupportedOperationException) {
            val message = messageService.createSendMessage(
                    chatId.toString(),
                    "message.not.supported"
            )

            execute(message)
        }
    }

    fun sendMessages(messages: List<Validable>) {
        try {
            messages.forEach {
                when (it.javaClass) {
                    AnswerCallbackQuery::class.java -> execute(it as AnswerCallbackQuery)
                    SendMessage::class.java -> execute(it as SendMessage)
                    DeleteMessage::class.java -> execute(it as DeleteMessage)
                    SendMediaGroup::class.java -> execute(it as SendMediaGroup)
                    EditMessageReplyMarkup::class.java -> execute(it as EditMessageReplyMarkup)
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to send messages", e)
        }
    }

    private fun updateSettings(user: User) {
        Setting.values().forEach {
            if (!user.settings.containsKey(it)) {
                user.settings[it] = it.defaultValue
            }
        }
    }

    private fun getPersonalData(update: Update): PersonalData {
        return when (getInputMessageType(update)) {
            MessageType.MESSAGE -> {
                PersonalData(
                        update.message.chat.firstName,
                        update.message.chat.lastName,
                        update.message.chat.userName
                )
            }
            MessageType.CALLBACK -> {
                PersonalData(
                        update.callbackQuery.from.firstName,
                        update.callbackQuery.from.lastName,
                        update.callbackQuery.from.userName
                )
            }
        }
    }

    private fun getChatId(update: Update): Long {
        return when (getInputMessageType(update)) {
            MessageType.MESSAGE -> update.message.chatId
            MessageType.CALLBACK -> update.callbackQuery.message.chat.id
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

    fun sendMessageText(chatId: Long?, text: String?) {
        val message = SendMessage().apply {
            enableMarkdown(true)
            this.chatId = chatId.toString()
            this.text = text!!
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