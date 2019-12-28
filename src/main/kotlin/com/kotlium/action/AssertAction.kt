package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.ASSERT
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

data class PageAssertAction(val assert: WebDriver.() -> Unit) : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        try {
            iWebDriverWrapper.driver.assert()
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                isOk = true,
                type = ASSERT,
                message = listOf()
            )
        } catch (e: Exception) {
            val message = e.message
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                isOk = false,
                type = ASSERT,
                message = if (message != null) listOf(message) else listOf()
            )
        }
    }

}

data class WaitForUrlAssertAction(val url: String) : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        try {
            WebDriverWait(iWebDriverWrapper.driver, 5).until(ExpectedConditions.urlToBe(url))
            return ActionExecuteResult(
                actionClass = WaitForUrlAssertAction::class,
                isOk = true,
                type = ASSERT,
                message = listOf("transition url to $url")
            )
        } catch (e: Exception) {
            return ActionExecuteResult(
                actionClass = WaitForUrlAssertAction::class,
                isOk = false,
                type = ASSERT,
                message = listOfNotNull("failed to transition to $url", e.message)
            )
        }
    }

}
