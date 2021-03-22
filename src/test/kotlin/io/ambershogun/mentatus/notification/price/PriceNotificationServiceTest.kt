package io.ambershogun.mentatus.notification.price

import io.ambershogun.mentatus.AbstractTest
import io.ambershogun.mentatus.notification.price.exception.StockNotFoundException
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.CollectionUtils
import yahoofinance.Stock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PriceNotificationServiceTest : AbstractTest() {

    @Autowired
    lateinit var priceNotificationService: PriceNotificationService

    @Test(expected = StockNotFoundException::class)
    fun `when failed to find correct ticker then exception`() {
        Mockito.`when`(stockService.getStock(anyString())).thenReturn(null)

        priceNotificationService.createNotification(1, "sber > 100")
    }

    @Test
    fun `when create with wrong ticker then try to find corrent`() {
        Mockito.`when`(stockService.getStock(anyString())).thenReturn(Stock("SBER.ME").apply { currency = "USD" })

        val notification = priceNotificationService.createNotification(1, "sber > 100")

        assertEquals("SBER.ME", notification.ticker)
    }

    @Test
    fun `create notification`() {
        priceNotificationService.createNotification(1, "sber > 300.02")

        val notification = priceNotificationRepository.findAll().first()
        assertEquals(1, notification.chatId)
        assertEquals("SBER", notification.ticker)
        assertEquals(EquitySign.GREATER, notification.equitySign)
        assertEquals(300.02, notification.price)
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
        run {
            priceNotificationService.createNotification(1, "mrna>115.44")

            assertTrue(CollectionUtils.isEmpty(priceNotificationService.findNotifications("MRNA", EquitySign.LESS, 120.00)))

            val notifications = priceNotificationService.findNotifications("MRNA", EquitySign.GREATER, 120.00)
            assertEquals(1, notifications.size)
            assertEquals("MRNA", notifications.first().ticker)
            assertEquals(115.44, notifications.first().price)

            priceNotificationRepository.deleteAll()
        }

        run {
            priceNotificationService.createNotification(1, "mrna <125.593")

            assertTrue(CollectionUtils.isEmpty(priceNotificationService.findNotifications("MRNA", EquitySign.GREATER, 120.00)))

            val notifications = priceNotificationService.findNotifications("MRNA", EquitySign.LESS, 120.00)
            assertEquals(1, notifications.size)
            assertEquals("MRNA", notifications.first().ticker)
            assertEquals(125.59, notifications.first().price)

            priceNotificationRepository.deleteAll()
        }

        run {
            priceNotificationService.createNotification(1, "AAPL >  50")

            assertTrue(CollectionUtils.isEmpty(priceNotificationService.findNotifications("AAPL", EquitySign.LESS, 64.00)))

            val notifications = priceNotificationService.findNotifications("AAPL", EquitySign.GREATER, 64.00)
            assertEquals(1, notifications.size)
            assertEquals("AAPL", notifications.first().ticker)
            assertEquals(50.00, notifications.first().price)

            priceNotificationRepository.deleteAll()
        }

        run {
            priceNotificationService.createNotification(1, "nok   < 10")

            assertTrue(CollectionUtils.isEmpty(priceNotificationService.findNotifications("NOK", EquitySign.GREATER, 6.0)))

            val notifications = priceNotificationService.findNotifications("NOK", EquitySign.LESS, 6.0)
            assertEquals(1, notifications.size)
            assertEquals("NOK", notifications.first().ticker)
            assertEquals(10.00, notifications.first().price)

            priceNotificationRepository.deleteAll()
        }
    }
}