package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.ASSERT
import org.openqa.selenium.WebDriver

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
