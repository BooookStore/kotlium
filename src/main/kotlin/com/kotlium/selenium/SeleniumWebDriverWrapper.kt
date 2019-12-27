package com.kotlium.selenium

import com.kotlium.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class SeleniumWebDriverWrapper(private val driver: WebDriver) : IWebDriverWrapper {

    override fun click(selector: Selector): Boolean {
        return try {
            driver.findElementUntilVisible(selector).click()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun input(selector: Selector, value: String): Boolean {
        return try {
            driver.findElementUntilVisible(selector).sendKeys(value)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isTextDisplay(value: String): Boolean {
        return try {
            driver.findElementUntilVisible(XPath("""//*[contains(./text(), "$value"]"""))
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

    private fun WebDriver.findElementUntilVisible(selector: Selector): WebElement {
        val element = when (selector) {
            is CssClass -> this.findElement(By.className(selector.value))
            is Id -> this.findElement(By.id(selector.value))
            is XPath -> this.findElement(By.xpath(selector.value))
        }
        WebDriverWait(this, 5).until(ExpectedConditions.visibilityOf(element))
        return element
    }

}