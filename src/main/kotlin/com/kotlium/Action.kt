package com.kotlium

interface Action {

    fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult

}

abstract class SingleTargetAction(open var target: Selector?) : Action {

    fun cssClass(value: String) {
        target = CssClass(value)
    }

    fun id(value: String) {
        target = Id(value)
    }

}

data class ClickAction(override var target: Selector? = null) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.click(target ?: throw IllegalStateException("click target is null"))
        return ActionExecuteResult(ClickAction::class, isOk, "click by id. value is ${target?.value}")
    }

}

data class InputAction(override var target: Selector? = null, var value: String? = null) : SingleTargetAction(target) {

    fun value(value: String) {
        this.value = value
    }

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(
            selector = target ?: throw IllegalStateException("input target is null"),
            value = value ?: throw IllegalStateException("input value is null")
        )
        return ActionExecuteResult(InputAction::class, isOk, null)
    }

}

class InputCard : Action {

    lateinit var cvv: String

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = iWebDriverWrapper.input(Id("cvv"), cvv)
        return ActionExecuteResult(InputCard::class, isOk, null)
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
        return ActionExecuteResult(ClickRegisterCard::class, isOk, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClickRegisterCard) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
