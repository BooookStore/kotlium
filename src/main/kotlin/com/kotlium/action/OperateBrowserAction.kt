package com.kotlium.action

import com.kotlium.action.BrowserActionType.OPERATOR
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory

data class TransitionBrowserAction(var url: String? = null) : BrowserAction {

    private val logger = LoggerFactory.getLogger(TransitionBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): BrowserActionExecuteResult {
        return try {
            webDriver.get(checkNotNull(url) { "url is null" })
            logger.info("transition {}", url)
            BrowserActionExecuteResult(this::class, OPERATOR, true, listOf("transition $url"))
        } catch (e: Exception) {
            logger.error("transition failed {}", url, e)
            BrowserActionExecuteResult(this::class, OPERATOR, false, listOf("transition failed $url"))
        }
    }

}

class SessionCloseBrowserAction : BrowserAction {

    private val logger = LoggerFactory.getLogger(SessionCloseBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): BrowserActionExecuteResult {
        return try {
            webDriver.close()
            logger.info("close session")
            BrowserActionExecuteResult(this::class, OPERATOR, true, listOf("close session"))
        } catch (e: Exception) {
            logger.error("failed close session", e)
            BrowserActionExecuteResult(this::class, OPERATOR, false, listOf("failed close session"))
        }
    }

}

abstract class SingleTargetBrowserAction(open var target: By?) : BrowserAction

data class ClickBrowserAction(override var target: By?) : SingleTargetBrowserAction(target) {

    private val logger = LoggerFactory.getLogger(ClickBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): BrowserActionExecuteResult {
        return try {
            webDriver.findElement(checkNotNull(target) { "click target is null" }).click()
            logger.info("click [{}]", target)
            BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click $target"))
        } catch (e: Exception) {
            logger.error("click failed [{}]", target, e)
            BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, false, listOf("click failed $target"))
        }
    }

}

data class InputBrowserAction(override var target: By? = null, var value: String? = null, var lastEnter: Boolean = false) :
    SingleTargetBrowserAction(target) {

    private val logger = LoggerFactory.getLogger(InputBrowserAction::class.java)

    override fun execute(webDriver: WebDriver): BrowserActionExecuteResult {
        return try {
            val inputContent = checkNotNull(value) { "input value is null" } + if (lastEnter) Keys.ENTER else ""
            webDriver.findElement(checkNotNull(target) { "input target is null" }).sendKeys(inputContent)
            logger.info("input [{}] to [{}]. enter is [{}]", value, target, lastEnter)
            BrowserActionExecuteResult(
                this::class, OPERATOR, true, listOf(
                    "input $value to $target",
                    "enter is $lastEnter"
                )
            )
        } catch (e: Exception) {
            logger.error("input failed [{}] to [{}]. enter is [{}]", value, target, lastEnter, e)
            BrowserActionExecuteResult(
                this::class, OPERATOR, false, listOf(
                    "input failed $value to $target",
                    "enter is $lastEnter"
                )
            )
        }
    }

}
