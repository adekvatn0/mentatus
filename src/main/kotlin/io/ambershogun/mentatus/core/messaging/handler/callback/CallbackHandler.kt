package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.BotApiMethod

interface CallbackHandler {
    fun dataRegEx(): String

    fun handleCallback(chatId: Long, callbackQueryId: String, messageId: Int, data: String): List<BotApiMethod<Boolean>>

    @Autowired
    fun selfRegister(registry: HandlerRegistry) {
        registry.register(this)
    }
}