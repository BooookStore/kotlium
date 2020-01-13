package com.kotlium.action

import com.kotlium.action.ActionType.ASSERT
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory

data class PageAssertBrowserAction(val assert: WebDriver.() -> Unit) : BrowserAction {

    private val logger = LoggerFactory.getLogger(PageAssertBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        try {
            webDriver.assert()
            logger.info("assertion filled")
            return ActionExecuteResult(
                browserActionClass = PageAssertBrowserAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf("assert filled")
            )
        } catch (e: Exception) {
            logger.error("assertion failed", e)
            return ActionExecuteResult(
                browserActionClass = PageAssertBrowserAction::class,
                type = ASSERT,
                isOk = false,
                message = listOfNotNull("assert failed", e.message)
            )
        }
    }

}

data class WaitForAssertBrowserAction<T>(val condition: ExpectedCondition<T>) : BrowserAction {

    private val logger = LoggerFactory.getLogger(WaitForAssertBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        try {
            WebDriverWait(webDriver, 5).until(condition)
            logger.info("expected wait condition filled")
            return ActionExecuteResult(
                browserActionClass = WaitForAssertBrowserAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf("expected wait condition filled")
            )
        } catch (e: Exception) {
            logger.error("expected wait condition not filled", e)
            return ActionExecuteResult(
                browserActionClass = WaitForAssertBrowserAction::class,
                type = ASSERT,
                isOk = false,
                message = listOfNotNull("expected wait condition not filled", e.message)
            )
        }
    }

}
