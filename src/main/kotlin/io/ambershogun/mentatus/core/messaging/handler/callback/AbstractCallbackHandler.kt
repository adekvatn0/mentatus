package io.ambershogun.mentatus.core.messaging.handler.callback

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.handler.MessageHandler
import io.ambershogun.mentatus.core.messaging.util.ResponseService
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.objects.Update
import java.net.URI
import kotlin.streams.toList

abstract class AbstractCallbackHandler : MessageHandler {

    @Autowired
    protected lateinit var responseService: ResponseService

    override fun handleMessage(user: User, update: Update): List<Validable> {
        return handleCallbackInternal(
                user.chatId,
                update.callbackQuery.id,
                update.callbackQuery.message.messageId,
                parseParams(update.callbackQuery.data)
        )
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