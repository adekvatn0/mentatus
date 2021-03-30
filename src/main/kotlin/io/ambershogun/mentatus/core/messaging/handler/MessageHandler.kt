package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.user.User
import io.ambershogun.mentatus.core.messaging.HandlerRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.interfaces.Validable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.Serializable

interface MessageHandler {
    fun messageRegEx(): String

    fun handleMessage(user: User, update: Update): List<Validable>

    @Autowired
    fun selfRegister(registry: HandlerRegistry) {
        registry.register(this)
    }
}