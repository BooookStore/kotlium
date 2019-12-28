package com.kotlium.selenium

import com.kotlium.IWebDriverWrapper
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class SeleniumWebDriverWrapper(private val driver: WebDriver) : IWebDriverWrapper {

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

    override fun isTextDisplay(value: String): Boolean {
        return try {
            driver.findElementUntilVisible(By.xpath("""//*[normalize-space() = '$value']"""))
            true
        } catch (e: Exception) {
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
        val element = driver.findElement(by)
        WebDriverWait(this, 5).until(ExpectedConditions.visibilityOf(element))
        return element
    }

}