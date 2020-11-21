package io.ambershogun.mentatus.notification.price

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.notification.price.exception.StockNotFoundException
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import kotlin.test.assertEquals

class PriceNotificationServiceTest : AbstractTest() {

    @Autowired
    lateinit var priceNotificationService: PriceNotificationService

    @Test(expected = StockNotFoundException::class)
    fun `when failed to find correct ticker then exception`() {
        Mockito.`when`(stockService.getYahooFinanceTickerName(anyString())).thenReturn(null)

        priceNotificationService.createNotification(1, "sber > 100")
    }

    @Test
    fun `when create with wrong ticker then try to find corrent`() {
        Mockito.`when`(stockService.getYahooFinanceTickerName(anyString())).thenReturn("SBER.ME")

        val notification = priceNotificationService.createNotification(1, "sber > 100")

        assertEquals("SBER.ME", notification.ticker)
    }

    @Test
    fun `create notification`() {
        priceNotificationService.createNotification(1, "sber > 300")

        val notification = priceNotificationRepository.findAll().first()
        assertEquals(1, notification.chatId)
        assertEquals("SBER", notification.ticker)
        assertEquals(EquitySign.GREATER, notification.equitySign)
        assertEquals(BigDecimal.valueOf(300).setScale(2), notification.price)
    }

    @Test
    fun `get distinct tickers`() {
        priceNotificationService.createNotification(1, "mrna <125.593")
        priceNotificationService.createNotification(1, "mrna>115.44")
        priceNotificationService.createNotification(1, "aapl   < 80,2")
        priceNotificationService.createNotification(1, "AAPL >  50")
        priceNotificationService.createNotification(1, "aflt > 1")

        val tickers = priceNotificationService.getDistinctTickers()
        assertEquals(3, tickers.size)
        assertEquals("MRNA", tickers[0])
        assertEquals("AAPL", tickers[1])
        assertEquals("AFLT", tickers[2])
    }

    @Test
    fun `get equity sign`() {
        assertEquals(EquitySign.LESS, priceNotificationService.getEquitySign("aflt < 123"))
        assertEquals(EquitySign.GREATER, priceNotificationService.getEquitySign("aflt > 123"))
    }

    @Test
    fun `find notifications`() {
        priceNotificationService.createNotification(1, "mrna <125.593")
        priceNotificationService.createNotification(1, "mrna>115.44")
        priceNotificationService.createNotification(1, "aapl   < 80,2")
        priceNotificationService.createNotification(1, "AAPL >  50")

        run {
            val notifications = priceNotificationService.findNotifications("MRNA", BigDecimal.valueOf(120.00), EquitySign.GREATER)
            assertEquals(1, notifications.size)
            assertEquals("MRNA", notifications.first().ticker)
            assertEquals(BigDecimal.valueOf(115.44), notifications.first().price)
        }

        run {
            val notifications = priceNotificationService.findNotifications("MRNA", BigDecimal.valueOf(120.00), EquitySign.LESS)
            assertEquals(1, notifications.size)
            assertEquals("MRNA", notifications.first().ticker)
            assertEquals(BigDecimal.valueOf(125.59), notifications.first().price)
        }

        run {
            val notifications = priceNotificationService.findNotifications("AAPL", BigDecimal.valueOf(64.00), EquitySign.GREATER)
            assertEquals(1, notifications.size)
            assertEquals("AAPL", notifications.first().ticker)
            assertEquals(BigDecimal.valueOf(50.00).setScale(2), notifications.first().price)
        }

        run {
            val notifications = priceNotificationService.findNotifications("AAPL", BigDecimal.valueOf(64.00), EquitySign.LESS)
            assertEquals(1, notifications.size)
            assertEquals("AAPL", notifications.first().ticker)
            assertEquals(BigDecimal.valueOf(80.20).setScale(2), notifications.first().price)
        }
    }
}