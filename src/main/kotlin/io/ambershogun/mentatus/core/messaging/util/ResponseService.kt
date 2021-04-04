package io.ambershogun.mentatus.core.messaging.util

import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.*

@Service
class ResponseService(
        private val messageSource: MessageSource,
) {

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