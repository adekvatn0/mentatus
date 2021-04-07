package io.ambershogun.mentatus.push.service

import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.entity.user.service.UserService
import io.ambershogun.mentatus.push.dto.BroadcastPush
import io.ambershogun.mentatus.push.dto.DirectPush
import io.ambershogun.mentatus.push.dto.PushResult
import io.ambershogun.mentatus.push.exception.UserNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PushService(
        private val userService: UserService,
        private val mentatusBot: MentatusBot
) {

    private final val logger: Logger = LoggerFactory.getLogger("push")

    fun sendDirectPush(push: DirectPush): PushResult {
        val user = userService.findByUsername(push.username!!) ?: throw UserNotFoundException(push.username!!)
        mentatusBot.sendMessageText(user.chatId, push.text)

        return PushResult(1)
    }

    fun sendBroadcastPush(push: BroadcastPush): PushResult {
        val users = userService.findAll()

        var count = 0
        users.forEach {
            mentatusBot.sendMessageText(it.chatId, push.text)
            count++
        }

        return PushResult(count)
    }
}