package io.ambershogun.mentatus.message.util

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*

object KeyboardCreator {

    fun createReplyKeyboard(buttons: Array<Array<String?>>): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        val keyboard: MutableList<KeyboardRow> = ArrayList()
        for (row in buttons) {
            val keyboardRow = KeyboardRow()
            for (y in row.indices) {
                keyboardRow.add(row[y])
            }
            keyboard.add(keyboardRow)
        }
        replyKeyboardMarkup.keyboard = keyboard
        return replyKeyboardMarkup
    }

    fun createNotificationDeleteButton(notificationId: String?): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
                listOf(
                        listOf(InlineKeyboardButton().apply {
                            text = "\uD83D\uDD15 Удалить"
                            callbackData = "delete:$notificationId"
                        })
                )
        )
    }
}