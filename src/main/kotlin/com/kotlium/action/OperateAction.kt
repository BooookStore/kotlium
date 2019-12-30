package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.OPERATOR
import org.openqa.selenium.By

data class TransitionAction(var url: String? = null) : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        return try {
            val driver = iWebDriverWrapper.driver
            driver.get(checkNotNull(url) { "url is null" })
            ActionExecuteResult(this::class, OPERATOR, true, listOf("transition $url"))
        } catch (e: Exception) {
            ActionExecuteResult(this::class, OPERATOR, false, listOf("transition failed $url"))
        }
    }

}

abstract class SingleTargetAction(open var target: By?) : Action

data class ClickAction(override var target: By?) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        return try {
            val driver = iWebDriverWrapper.driver
            driver.findElement(checkNotNull(target) { "click target is null" }).click()
            ActionExecuteResult(ClickAction::class, OPERATOR, true, listOf("click $target"))
        } catch (e: Exception) {
            ActionExecuteResult(ClickAction::class, OPERATOR, false, listOf("click failed $target"))
        }
    }

}

data class InputAction(
    override var target: By? = null,
    var value: String? = null,
    var lastEnter: Boolean = false
) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(
            by = checkNotNull(target) { "input target is null" },
            value = checkNotNull(value) { "input value is null" },
            lastEnter = lastEnter
        )
        return ActionExecuteResult(InputAction::class, OPERATOR, isOk, listOf())
    }

}
