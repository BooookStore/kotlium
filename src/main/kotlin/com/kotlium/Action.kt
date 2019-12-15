package com.kotlium

interface Action

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

data class ClickAction(override var target: Selector? = null) : SingleTargetAction(target)

data class InputAction(override var target: Selector? = null, var value: String? = null) : SingleTargetAction(target) {

    fun value(value: String) {
        this.value = value
    }

}

data class InputCard(var cvv: String? = null) : Action

class ClickRegisterCard : Action {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClickRegisterCard) return false
        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
