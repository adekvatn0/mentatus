package io.ambershogun.mentatus.core.marketmaps.selenium

import io.ambershogun.mentatus.core.properties.SeleniumProperties
import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit

@Service
@Profile("!test")
class SeleniumService(
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
        try {
            val chromeOptions = ChromeOptions()
            chromeOptions.addArguments("--window-size=${seleniumProperties.screenWidth},${seleniumProperties.screenHeight}")
            chromeOptions.addArguments("--headless")
            chromeOptions.addArguments("user-agent=${seleniumProperties.userAgent}")

            driver = ChromeDriver(chromeOptions)

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

            updateSectorsScreenshot()
            updateRegionsScreenshot()
        } catch (e: Exception) {
            logger.error("Failed to update market maps\n\n", e)
        } finally {
            driver.quit()
        }
    }

    private fun updateSectorsScreenshot() {
        driver.get("https://finviz.com/map.ashx")
        val map = driver.findElement(By.id("body"))
        val mapFile = map.getScreenshotAs(OutputType.FILE)
        FileUtils.copyFile(mapFile, File("$marketMapsDir/sectors.png"))
    }

    private fun updateRegionsScreenshot() {
        driver.get("https://finviz.com/map.ashx?t=geo")
        val map = driver.findElement(By.id("body"))
        val mapFile = map.getScreenshotAs(OutputType.FILE)
        FileUtils.copyFile(mapFile, File("$marketMapsDir/regions.png"))
    }
}

