package io.ambershogun.mentatus.core.message

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface MessageHandler {
    fun messageRegEx(): String

    @Transactional
    fun handleMessage(chatId: Long, languageCode: String, inputMessage: String): List<SendMessage>

    @Autowired
    fun selfRegister(registry: MessageHandlerRegistry) {
        registry.register(this)
    }
}