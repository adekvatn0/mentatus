package io.ambershogun.mentatus.core.messaging.util

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*

@Service
class KeyboardService {

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
}