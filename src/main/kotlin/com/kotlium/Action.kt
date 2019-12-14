package com.kotlium

interface Action

data class ClickAction(var target: String? = null) : Action

data class InputAction(var target: String? = null, var value: String? = null) : Action

data class InputCard(var cvv: String? = null) : Action

