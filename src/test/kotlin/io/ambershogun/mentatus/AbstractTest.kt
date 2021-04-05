package io.ambershogun.mentatus

import io.ambershogun.mentatus.core.notification.price.threshold.repo.PriceThresholdRepository
import io.ambershogun.mentatus.core.notification.price.threshold.service.StockService
import io.ambershogun.mentatus.core.entity.user.repo.UserRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import yahoofinance.Stock

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
abstract class AbstractTest {

    @Autowired
    protected lateinit var priceThresholdRepository: PriceThresholdRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @MockBean
    protected lateinit var stockService: StockService

    @Before
    fun init() {
        priceThresholdRepository.deleteAll()
        userRepository.deleteAll()

        Mockito.`when`(stockService.getStock(ArgumentMatchers.anyString())).thenAnswer {
            val args: Array<Any> = it.arguments
            return@thenAnswer Stock((args[0] as String).toUpperCase()).apply { currency = "USD" }
        }
    }

    @After
    fun cleanUp() {
        priceThresholdRepository.deleteAll()
    }
}