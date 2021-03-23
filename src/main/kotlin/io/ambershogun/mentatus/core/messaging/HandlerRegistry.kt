package io.ambershogun.mentatus.core.messaging

import io.ambershogun.mentatus.core.messaging.handler.callback.CallbackHandler
import io.ambershogun.mentatus.core.messaging.handler.message.MessageHandler
import org.springframework.stereotype.Service

@Service
class HandlerRegistry {

    private var messageHandlers: HashMap<String, MessageHandler> = HashMap()

    private var callbackHandlers: HashMap<String, CallbackHandler> = HashMap()

    fun register(messageHandler: MessageHandler) {
        messageHandlers[messageHandler.messageRegEx()] = messageHandler
    }

    fun register(callbackHandler: CallbackHandler) {
        callbackHandlers[callbackHandler.dataRegEx()] = callbackHandler
    }

    fun getMessageHandler(inputMessage: String): MessageHandler {
        return messageHandlers.entries.stream()
                .filter { e -> inputMessage.toLowerCase().matches(Regex(e.value.messageRegEx())) }
                .findFirst()
                .map { e -> e.value }
                .orElseThrow { UnsupportedOperationException(inputMessage) }
    }

    fun getCallbackHandler(data: String): CallbackHandler {
        return callbackHandlers.entries.stream()
                .filter { e -> data.toLowerCase().matches(Regex(e.value.dataRegEx())) }
                .findFirst()
                .map { e -> e.value }
                .orElseThrow { UnsupportedOperationException(data) }
    }
}