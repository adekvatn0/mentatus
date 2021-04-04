package io.ambershogun.mentatus.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.UnsynchronizedAppenderBase
import io.ambershogun.mentatus.core.MentatusBot
import io.ambershogun.mentatus.core.properties.AppProperties
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component

@Component
class AdminTelegramLogAppender(
        private val appPropertis: AppProperties,
        private val bot: MentatusBot
) : UnsynchronizedAppenderBase<ILoggingEvent>(), SmartLifecycle {
    override fun append(event: ILoggingEvent) {
        bot.sendMessageText(
                appPropertis.adminId,
                event.message + "\n\n" + ThrowableProxyUtil.asString(event.throwableProxy)
        )
    }

    override fun isRunning(): Boolean {
        return isStarted
    }
}