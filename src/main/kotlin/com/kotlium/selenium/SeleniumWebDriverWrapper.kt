package com.kotlium.selenium

import com.kotlium.IWebDriverWrapper
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class SeleniumWebDriverWrapper(override val driver: WebDriver) : IWebDriverWrapper {

    override fun click(by: By): Boolean {
        return try {
            driver.findElementUntilVisible(by).click()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun input(by: By, value: String, lastEnter: Boolean): Boolean {
        return try {
            driver.findElementUntilVisible(by).sendKeys(value + if (lastEnter) Keys.ENTER else "")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun get(url: String): Boolean {
        return try {
            driver.get(url)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun currentUrl(): String {
        return driver.currentUrl
    }

    override fun deleteSession() {
        driver.close()
    }

    private fun WebDriver.findElementUntilVisible(by: By): WebElement {
        return driver.findElement(by)
    }

}