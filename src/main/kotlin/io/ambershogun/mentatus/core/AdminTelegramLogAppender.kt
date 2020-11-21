package io.ambershogun.mentatus.core

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.UnsynchronizedAppenderBase
import io.ambershogun.mentatus.MentatusBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component

@Component
class AdminTelegramLogAppender(
        @Value("\${mentatus.admin-id}") private val adminChatId: Long,
        private val bot: MentatusBot
) : UnsynchronizedAppenderBase<ILoggingEvent>(), SmartLifecycle {
    override fun append(event: ILoggingEvent) {
        ThrowableProxyUtil.asString(event.throwableProxy)
        bot.sendMessageText(
                adminChatId,
                event.message + "\n\n" + ThrowableProxyUtil.asString(event.throwableProxy)
        )
    }

    override fun isRunning(): Boolean {
        return isStarted
    }
}