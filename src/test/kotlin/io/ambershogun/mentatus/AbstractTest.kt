package io.ambershogun.mentatus

import io.ambershogun.mentatus.core.entity.notification.price.repo.PriceNotificationRepository
import io.ambershogun.mentatus.core.entity.notification.price.service.StockService
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import yahoofinance.Stock

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

        Mockito.`when`(stockService.getStock(ArgumentMatchers.anyString())).thenAnswer {
            val args: Array<Any> = it.arguments
            return@thenAnswer Stock(args[0] as String).apply { currency = "USD" }
        }
    }

    @After
    fun cleanUp() {
        priceNotificationRepository.deleteAll()
    }
}