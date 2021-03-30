package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.util.ResponseService
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import java.io.Serializable
import java.time.LocalDateTime

abstract class AbstractMessageHandler : MessageHandler {

    @Autowired
    protected lateinit var userService: UserService

    @Autowired
    protected lateinit var responseService: ResponseService

    final override fun handleMessage(chatId: Long, inputMessage: String): List<Validable> {
        val user = userService.findOrCreateUser(chatId)

        user.lastActive = LocalDateTime.now()
        userService.saveUser(user)

        return handleMessageInternal(user, inputMessage)
    }

    protected abstract fun handleMessageInternal(user: User, inputMessage: String): List<Validable>
}