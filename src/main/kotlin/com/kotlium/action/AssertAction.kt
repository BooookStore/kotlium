package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.ASSERT
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

data class PageAssertAction(val assert: WebDriver.() -> Unit) : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        try {
            iWebDriverWrapper.driver.assert()
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf()
            )
        } catch (e: Exception) {
            val message = e.message
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                type = ASSERT,
                isOk = false,
                message = if (message != null) listOf(message) else listOf()
            )
        }
    }

}

data class WaitForAssertAction<T>(val condition: ExpectedCondition<T>) : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        try {
            WebDriverWait(iWebDriverWrapper.driver, 5).until(condition)
            return ActionExecuteResult(
                actionClass = WaitForAssertAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf("expected wait condition filled")
            )
        } catch (e: Exception) {
            return ActionExecuteResult(
                actionClass = WaitForAssertAction::class,
                type = ASSERT,
                isOk = false,
                message = listOfNotNull("expected wait condition not filled", e.message)
            )
        }
    }

}
