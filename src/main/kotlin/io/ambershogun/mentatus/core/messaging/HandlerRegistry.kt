package io.ambershogun.mentatus.core.messaging

import io.ambershogun.mentatus.core.messaging.handler.MessageHandler
import io.ambershogun.mentatus.core.util.MessageType
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class HandlerRegistry {

    private var messageHandlers: HashMap<String, MessageHandler> = HashMap()

    fun register(messageHandler: MessageHandler) {
        messageHandlers[messageHandler.messageRegEx()] = messageHandler
    }

    fun getHandler(messageType: MessageType, update: Update): MessageHandler {
        val inputMessage = when (messageType) {
            MessageType.MESSAGE -> update.message.text
            MessageType.CALLBACK -> update.callbackQuery.data
        }

        return messageHandlers.entries.stream()
                .filter { e -> inputMessage.matches(Regex(e.value.messageRegEx())) }
                .findFirst()
                .map { e -> e.value }
                .orElseThrow { UnsupportedOperationException(inputMessage) }
    }
}