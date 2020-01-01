package com.kotlium.action

import com.kotlium.action.ActionType.OPERATOR
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory

data class TransitionAction(var url: String? = null) : Action {

    private val logger = LoggerFactory.getLogger(TransitionAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        return try {
            webDriver.get(checkNotNull(url) { "url is null" })
            logger.info("transition {}", url)
            ActionExecuteResult(this::class, OPERATOR, true, listOf("transition $url"))
        } catch (e: Exception) {
            logger.warn("transition failed {}", url)
            ActionExecuteResult(this::class, OPERATOR, false, listOf("transition failed $url"))
        }
    }

}

class SessionCloseAction : Action {

    private val logger = LoggerFactory.getLogger(SessionCloseAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        return try {
            webDriver.close()
            logger.info("close session")
            ActionExecuteResult(this::class, OPERATOR, true, listOf("close session"))
        } catch (e: Exception) {
            logger.warn("failed close session")
            ActionExecuteResult(this::class, OPERATOR, false, listOf("failed close session"))
        }
    }

}

abstract class SingleTargetAction(open var target: By?) : Action

data class ClickAction(override var target: By?) : SingleTargetAction(target) {

    private val logger = LoggerFactory.getLogger(ClickAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        return try {
            webDriver.findElement(checkNotNull(target) { "click target is null" }).click()
            logger.info("click [{}]", target)
            ActionExecuteResult(ClickAction::class, OPERATOR, true, listOf("click $target"))
        } catch (e: Exception) {
            logger.warn("click failed [{}]", target)
            ActionExecuteResult(ClickAction::class, OPERATOR, false, listOf("click failed $target"))
        }
    }

}

data class InputAction(override var target: By? = null, var value: String? = null, var lastEnter: Boolean = false) :
    SingleTargetAction(target) {

    private val logger = LoggerFactory.getLogger(InputAction::class.java)

    override fun execute(webDriver: WebDriver): ActionExecuteResult {
        return try {
            val inputContent = checkNotNull(value) { "input value is null" } + if (lastEnter) Keys.ENTER else ""
            webDriver.findElement(checkNotNull(target) { "input target is null" }).sendKeys(inputContent)
            logger.info("input [{}] to [{}]. enter is [{}]", value, target, lastEnter)
            ActionExecuteResult(
                this::class, OPERATOR, true, listOf(
                    "input $value to $target",
                    "enter is $lastEnter"
                )
            )
        } catch (e: Exception) {
            logger.info("input failed [{}] to [{}]. enter is [{}]", value, target, lastEnter)
            ActionExecuteResult(
                this::class, OPERATOR, false, listOf(
                    "input failed $value to $target",
                    "enter is $lastEnter"
                )
            )
        }
    }

}
