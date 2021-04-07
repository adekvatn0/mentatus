package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.handler.MessageHandler
import io.ambershogun.mentatus.core.messaging.util.KeyboardService
import io.ambershogun.mentatus.core.messaging.util.MessageService
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractMessageHandler : MessageHandler {

    @Autowired
    protected lateinit var messageService: MessageService

    @Autowired
    protected lateinit var userService: UserService

    @Autowired
    protected lateinit var keyboardService: KeyboardService
}