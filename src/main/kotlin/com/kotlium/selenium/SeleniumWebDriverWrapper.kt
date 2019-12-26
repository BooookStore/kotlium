package com.kotlium.selenium

import com.kotlium.CssClass
import com.kotlium.IWebDriverWrapper
import com.kotlium.Id
import com.kotlium.Selector
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

    private fun WebDriver.findElementUntilVisible(selector: Selector): WebElement {
        val element = when (selector) {
            is CssClass -> this.findElement(By.className(selector.value))
            is Id -> this.findElement(By.id(selector.value))
        }
        WebDriverWait(this, 10).until(ExpectedConditions.visibilityOf(element))
        return element
    }

}