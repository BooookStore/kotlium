package com.kotlium.selenium

import com.kotlium.CssClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class SeleniumWebDriverWrapperTest {

    private lateinit var seleniumWebDriverWrapper: SeleniumWebDriverWrapper

    private lateinit var driver: WebDriver

    @BeforeEach
    fun beforeEach() {
        driver = RemoteWebDriver(URL("http://localhost:4444/wd/hub"), ChromeOptions())
        driver.get("https://selenium.dev/")
        seleniumWebDriverWrapper = SeleniumWebDriverWrapper(driver)
    }

    @AfterEach
    fun afterEach() {
        driver.close()
    }

    @Test
    fun clickTest() {
        // execute
        seleniumWebDriverWrapper.click(CssClass("webdriver"))

        // verify
        assertThat(driver.currentUrl).isEqualTo("https://selenium.dev/downloads/")
    }

}