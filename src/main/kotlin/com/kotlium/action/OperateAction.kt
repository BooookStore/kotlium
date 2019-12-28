package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.OPERATOR
import org.openqa.selenium.By

abstract class SingleTargetAction(open var target: By?) : Action

data class ClickAction(override var target: By? = null) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.click(checkNotNull(target) { "click target is null" })
        return ActionExecuteResult(ClickAction::class, isOk, OPERATOR, listOf("click $target"))
    }

}

data class InputAction(
    override var target: By? = null,
    var value: String? = null,
    var inputEnter: Boolean = false
) : SingleTargetAction(target) {

    fun inputEnter() {
        inputEnter = true
    }

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(
            by = checkNotNull(target) { "input target is null" },
            value = checkNotNull(value) { "input value is null" },
            lastEnter = inputEnter
        )
        return ActionExecuteResult(InputAction::class, isOk, OPERATOR, listOf())
    }

}

class InputCard : Action {

    lateinit var cvv: String

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(By.id("cvv"), cvv, false)
        return ActionExecuteResult(InputCard::class, isOk, OPERATOR, listOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InputCard) return false
        if (cvv != other.cvv) return false
        return true
    }

    override fun hashCode(): Int {
        return cvv.hashCode()
    }

}

class ClickRegisterCard : Action {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.click(By.id("register"))
        return ActionExecuteResult(ClickRegisterCard::class, isOk, OPERATOR, listOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClickRegisterCard) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
