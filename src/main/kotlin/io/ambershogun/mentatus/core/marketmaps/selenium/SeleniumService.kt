package io.ambershogun.mentatus.core.marketmaps.selenium

import io.ambershogun.mentatus.core.marketmaps.FinvizImageService
import io.ambershogun.mentatus.core.properties.SeleniumProperties
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
@Profile("!test")
class SeleniumService(
        private val finvizImageService: FinvizImageService,
        private val seleniumProperties: SeleniumProperties,
        @Value("\${marketmaps.dir}") private val marketMapsDir: String
) {

    private val logger = LoggerFactory.getLogger("messaging")

    lateinit var driver: ChromeDriver

    init {
        System.setProperty("webdriver.chrome.driver", "$marketMapsDir/chromedriver")
    }

    @Async
    fun updateFinvizScreenshots() {
        val sectors = getImageUrl("https://finviz.com/map.ashx")
        val regions = getImageUrl("https://finviz.com/map.ashx?t=geo")

        finvizImageService.saveImageUrls(sectors, regions)
    }

    private fun getImageUrl(url: String): String {
        try {
            val chromeOptions = ChromeOptions()
            chromeOptions.addArguments("--window-size=${seleniumProperties.screenWidth},${seleniumProperties.screenHeight}")
            chromeOptions.addArguments("--headless")
            chromeOptions.addArguments("user-agent=${seleniumProperties.userAgent}")
            chromeOptions.setBinary(seleniumProperties.binaryPath)

            driver = ChromeDriver(chromeOptions)

            driver.manage().timeouts().implicitlyWait(seleniumProperties.waitSecs, TimeUnit.SECONDS)

            driver.get(url)

            val shareMapButton = driver.findElement(By.id("share-map"))

            shareMapButton.click()

            val imgUrlField = driver.findElement(By.id("static"))

            return imgUrlField.getAttribute("value")
        } catch (e: Exception) {
            logger.error("Failed to update market maps\n\n", e)
        } finally {
            driver.quit()
        }

        return ""
    }
}

