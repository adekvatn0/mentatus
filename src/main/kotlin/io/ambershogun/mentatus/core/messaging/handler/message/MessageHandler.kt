package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import java.io.Serializable

interface MessageHandler {
    fun messageRegEx(): String

    fun handleMessage(chatId: Long, inputMessage: String): List<Validable>

    @Autowired
    fun selfRegister(registry: HandlerRegistry) {
        registry.register(this)
    }

    fun isRetryable() = false
}