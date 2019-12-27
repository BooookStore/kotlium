package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.Id
import com.kotlium.Selector
import com.kotlium.action.ActionType.OPERATOR

abstract class SingleTargetAction(open var target: Selector?) : Action

data class ClickAction(override var target: Selector? = null) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.click(target ?: throw IllegalStateException("click target is null"))
        return ActionExecuteResult(ClickAction::class, isOk, OPERATOR, listOf("click by id. value is ${target?.value}"))
    }

}

data class InputAction(override var target: Selector? = null, var value: String? = null) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(
            selector = target ?: throw IllegalStateException("input target is null"),
            value = value ?: throw IllegalStateException("input value is null")
        )
        return ActionExecuteResult(InputAction::class, isOk, OPERATOR, listOf())
    }

}

class InputCard : Action {

    lateinit var cvv: String

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(Id("cvv"), cvv)
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
        val isOk = iWebDriverWrapper.click(Id("register"))
        return ActionExecuteResult(ClickRegisterCard::class, isOk, OPERATOR, listOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClickRegisterCard) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
