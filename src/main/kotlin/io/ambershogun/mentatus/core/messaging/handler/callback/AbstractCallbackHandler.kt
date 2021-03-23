package io.ambershogun.mentatus.core.messaging.handler.callback

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.net.URI
import kotlin.streams.toList

abstract class AbstractCallbackHandler : CallbackHandler {

    final override fun handleCallback(chatId: Long, callbackQueryId: String, messageId: Int, data: String): List<BotApiMethod<Boolean>> {
        return handleCallbackInternal(chatId, callbackQueryId, messageId, parseParams(data))
    }

    protected abstract fun handleCallbackInternal(
            chatId: Long,
            callbackQueryId: String,
            messageId: Int,
            params: Map<String, String>
    ): List<BotApiMethod<Boolean>>

    private fun parseParams(data: String): Map<String, String> {
        return URI(data).query.split("&").stream()
                .map {
                    val splited = it.split("=")
                    Pair(splited[0], splited[1])
                }.toList().toMap()
    }
}