package com.kotlium.action

import com.kotlium.action.ActionType.ASSERT
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory

data class PageAssertAction(val assert: WebDriver.() -> Unit) : Action {

    private val logger = LoggerFactory.getLogger(PageAssertAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        try {
            webDriver.assert()
            logger.info("assertion filled")
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf("assert filled")
            )
        } catch (e: Exception) {
            logger.error("assertion failed", e)
            return ActionExecuteResult(
                actionClass = PageAssertAction::class,
                type = ASSERT,
                isOk = false,
                message = listOfNotNull("assert failed", e.message)
            )
        }
    }

}

data class WaitForAssertAction<T>(val condition: ExpectedCondition<T>) : Action {

    private val logger = LoggerFactory.getLogger(WaitForAssertAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        try {
            WebDriverWait(webDriver, 5).until(condition)
            logger.info("expected wait condition filled")
            return ActionExecuteResult(
                actionClass = WaitForAssertAction::class,
                type = ASSERT,
                isOk = true,
                message = listOf("expected wait condition filled")
            )
        } catch (e: Exception) {
            logger.error("expected wait condition not filled", e)
            return ActionExecuteResult(
                actionClass = WaitForAssertAction::class,
                type = ASSERT,
                isOk = false,
                message = listOfNotNull("expected wait condition not filled", e.message)
            )
        }
    }

}
