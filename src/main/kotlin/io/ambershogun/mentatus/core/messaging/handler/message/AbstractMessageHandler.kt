package io.ambershogun.mentatus.core.messaging.handler.message

import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.core.messaging.handler.MessageHandler
import io.ambershogun.mentatus.core.messaging.util.ResponseService
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractMessageHandler : MessageHandler {

    @Autowired
    protected lateinit var responseService: ResponseService

    @Autowired
    protected lateinit var userService: UserService
}