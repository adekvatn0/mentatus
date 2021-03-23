package io.ambershogun.mentatus.message.handler

import io.ambershogun.mentatus.notification.price.EquitySign
import io.ambershogun.mentatus.notification.price.PriceNotification
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.stream.Collectors
import kotlin.test.assertEquals

class ListNotificationsMessageHandlerTest : AbstractMessageHandlerTest() {

    @Autowired
    lateinit var messageHandler: ListNotificationsMessageHandler

    override fun `test message regex`() {
        assertTrue("\uD83D\uDD14 уведомления".matches(Regex(messageHandler.messageRegEx())))
        assertFalse("any string".matches(Regex(messageHandler.messageRegEx())))
    }

    override fun `test handle message`() {
        run {
            val response = messageHandler.handleMessage(1, "ru", "list")
            assertEquals(
                    messageSource.getMessage("notification.list.empty", emptyArray(), Locale.forLanguageTag("ru")),
                    response[0].text
            )
        }

        run {
            val notification = priceNotificationRepository.save(PriceNotification(1, "MRNA", EquitySign.GREATER, 100.00, "USD"))

            val response = messageHandler.handleMessage(1, "ru", "list")
            val notificationsListString = listOf(notification).stream()
                    .map { it.toString() }
                    .collect(Collectors.joining("\n"))

            assertEquals(
                    messageSource.getMessage("notification.list", arrayOf(notificationsListString), Locale.forLanguageTag("ru")),
                    response[0].text
            )
        }
    }
}