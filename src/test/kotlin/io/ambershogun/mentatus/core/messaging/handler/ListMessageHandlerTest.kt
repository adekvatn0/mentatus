package io.ambershogun.mentatus.core.messaging.handler

import io.ambershogun.mentatus.core.entity.notification.price.EquitySign
import io.ambershogun.mentatus.core.entity.notification.price.PriceNotification
import io.ambershogun.mentatus.core.messaging.handler.message.ListNotificationsMessageHandler
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*
import java.util.stream.Collectors
import kotlin.test.assertEquals

class ListNotificationsMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: ListNotificationsMessageHandler

    override fun `test message regex`() {
        assertTrue("\uD83D\uDD14 Уведомления".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        run {
            val response = messageHandler.handleMessage(1, "list")
            assertEquals(
                    messageSource.getMessage("notification.list.empty", emptyArray(), Locale.forLanguageTag("ru")),
                    (response[0] as SendMessage).text
            )
        }

        run {
            val notification = priceNotificationRepository.save(PriceNotification(1, "MRNA", EquitySign.GREATER, 100.00, "USD"))

            val response = messageHandler.handleMessage(1, "list")
            val notificationsListString = listOf(notification).stream()
                    .map { it.toString() }
                    .collect(Collectors.joining("\n"))

            assertEquals(
                    messageSource.getMessage("notification.list", arrayOf(notificationsListString), Locale.forLanguageTag("ru")),
                    (response[0] as SendMessage).text
            )
        }
    }
}