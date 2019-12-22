package com.kotlium

interface Action {

    fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult

}

abstract class SingleTargetAction(open var target: Selector?) : Action {

    fun text(value: String) {
        target = Text(value)
    }

    fun `class`(value: String) {
        target = Class(value)
    }

    fun id(value: String) {
        target = Id(value)
    }

}

data class ClickAction(override var target: Selector? = null) : SingleTargetAction(target) {

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        return ActionExecuteResult(ClickAction::class, true, "click by id. value is ${target?.value}")
    }

}

data class InputAction(override var target: Selector? = null, var value: String? = null) : SingleTargetAction(target) {

    fun value(value: String) {
        this.value = value
    }

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        return ActionExecuteResult(InputAction::class, true, null)
    }

}

class InputCard : Action {

    lateinit var cvv: String

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        return ActionExecuteResult(InputCard::class, true, null)
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
        return ActionExecuteResult(ClickRegisterCard::class, true, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClickRegisterCard) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
