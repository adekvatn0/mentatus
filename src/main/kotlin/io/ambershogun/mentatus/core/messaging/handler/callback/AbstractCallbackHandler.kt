package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.messaging.util.ResponseService
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.net.URI
import kotlin.streams.toList

abstract class AbstractCallbackHandler : CallbackHandler {

    @Autowired
    protected lateinit var responseService: ResponseService

    final override fun handleCallback(chatId: Long, callbackQueryId: String, messageId: Int, data: String): List<Validable> {
        return handleCallbackInternal(chatId, callbackQueryId, messageId, parseParams(data))
    }

    protected abstract fun handleCallbackInternal(
            chatId: Long,
            callbackQueryId: String,
            messageId: Int,
            params: Map<String, String>
    ): List<Validable>

    private fun parseParams(data: String): Map<String, String> {
        return URI(data).query.split("&").stream()
                .map {
                    val splited = it.split("=")
                    Pair(splited[0], splited[1])
                }.toList().toMap()
    }
}