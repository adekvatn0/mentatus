package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.user.Setting
import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class ChangeSettingsCallbackHandler : AbstractCallbackHandler() {
    override fun messageRegEx(): String {
        return "^(\\/settings).*"
    }

    override fun handleCallbackInternal(user: User, update: Update, params: Map<String, String>): List<Validable> {
        val settingName = params["name"]!!
        val newValue = params["value"].toBoolean()

        val setting = Setting.valueOf(settingName)

        user.settings[setting] = newValue

        userService.saveUser(user)

        val pushMessage = responseService.createPushMessage(
                user.chatId.toString(),
                update.callbackQuery.id,
                "settings.changed"
        )

        val editSettingsMessage = EditMessageReplyMarkup().apply {
            chatId = user.chatId.toString()
            messageId = update.callbackQuery.message.messageId
            replyMarkup = responseService.createSettingsButtons(user.settings)
        }

        return listOf(pushMessage, editSettingsMessage)
    }
}
