package com.kotlium

interface Action

abstract class SingleTargetAction(open var target: Selector?) : Action {

    fun text(selectorConfigure: () -> String) {
        target = Text(selectorConfigure())
    }

    fun `class`(selectorConfigure: () -> String) {
        target = Class(selectorConfigure())
    }

    fun id(selectorConfigure: () -> String) {
        target = Id(selectorConfigure())
    }

}

data class ClickAction(override var target: Selector? = null) : SingleTargetAction(target)

data class InputAction(override var target: Selector? = null, var value: String? = null) : SingleTargetAction(target) {

    fun value(valueConfigure: () -> String) {
        value = valueConfigure()
    }

}

data class InputCard(var cvv: String? = null) : Action

