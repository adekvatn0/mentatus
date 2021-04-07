package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class SettingsMessageHandler : AbstractMessageHandler() {

    override fun messageRegEx(): String {
        return "⚙ Настройки"
    }

    override fun handleMessage(user: User, update: Update): List<Validable> {
        return listOf(
                messageService.createSettingsMessage(user)
        )
    }
}