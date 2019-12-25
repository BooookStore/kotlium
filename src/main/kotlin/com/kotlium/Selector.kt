package com.kotlium

abstract class Selector(open val value: String)

data class Text(override val value: String) : Selector(value)

data class CssClass(override val value: String) : Selector(value)

data class Id(override val value: String) : Selector(value)
