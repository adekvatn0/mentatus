package io.ambershogun.mentatus.core.marketmaps.selenium

import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit

@Service
class SeleniumService {

    lateinit var driver: ChromeDriver

    init {
        System.setProperty("webdriver.chrome.driver", "./target/classes/marketmaps/chromedriver")
    }

    @Async
    fun updateFinvizScreenshots() {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--window-size=1300,1300")

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
        FileUtils.copyFile(mapFile, File("./marketmaps/sectors.png"))
    }

    private fun updateRegionsScreenshot() {
        driver.get("https://finviz.com/map.ashx?t=geo")
        val map = driver.findElement(By.id("body"))
        val mapFile = map.getScreenshotAs(OutputType.FILE)
        FileUtils.copyFile(mapFile, File("./marketmaps/regions.png"))
    }
}

