package com.kotlium

interface Action

data class ClickAction(var target: Selector? = null) : Action {

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

data class InputAction(var target: String? = null, var value: String? = null) : Action

data class InputCard(var cvv: String? = null) : Action

