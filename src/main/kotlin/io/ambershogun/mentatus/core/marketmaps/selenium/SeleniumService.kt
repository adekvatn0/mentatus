package io.ambershogun.mentatus.core.marketmaps.selenium

import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit

@Service
class SeleniumService(
        @Value("\${marketmaps.dir}") private val marketMapsDir: String
) {

    lateinit var driver: ChromeDriver

    init {
        System.setProperty("webdriver.chrome.driver", "$marketMapsDir/chromedriver")
    }

    @Async
    fun updateFinvizScreenshots() {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--window-size=1300,1300")
        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")


        driver = ChromeDriver(chromeOptions)

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)

        updateSectorsScreenshot()
        updateRegionsScreenshot()

        driver.quit()
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

