package io.ambershogun.mentatus

import io.ambershogun.mentatus.notification.price.PriceNotificationRepository
import io.ambershogun.mentatus.notification.price.StockService
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
abstract class AbstractTest {

    @Autowired
    protected lateinit var priceNotificationRepository: PriceNotificationRepository

    @MockBean
    protected lateinit var stockService: StockService

    @Before
    fun init() {
        priceNotificationRepository.deleteAll()

        Mockito.`when`(stockService.getYahooFinanceTickerName(ArgumentMatchers.anyString())).thenAnswer {
            val args: Array<Any> = it.arguments
            return@thenAnswer args[0] as String
        }
    }

    @After
    fun cleanUp() {
        priceNotificationRepository.deleteAll()
    }
}