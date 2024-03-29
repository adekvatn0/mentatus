package io.ambershogun.mentatus

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import io.ambershogun.mentatus.logging.AdminTelegramLogAppender
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
class MentatusApplication

fun main(args: Array<String>) {
    val context = runApplication<MentatusApplication>(*args)
    addTelegramLogAppender(context)
}

fun addTelegramLogAppender(context: ConfigurableApplicationContext) {
    val customAppender = context.getBean(AdminTelegramLogAppender::class.java)
    val rootLogger: Logger = (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger("messaging")
    rootLogger.addAppender(customAppender)
}
