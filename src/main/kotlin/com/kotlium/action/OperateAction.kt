package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.OPERATOR
import org.openqa.selenium.By

abstract class SingleTargetAction(open var target: By?) : Action

data class ClickAction(override var target: By?) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.click(checkNotNull(target) { "click target is null" })
        return ActionExecuteResult(ClickAction::class, isOk, OPERATOR, listOf("click $target"))
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
        return ActionExecuteResult(InputAction::class, isOk, OPERATOR, listOf())
    }

}
