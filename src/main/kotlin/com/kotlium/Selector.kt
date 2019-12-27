package com.kotlium

sealed class Selector(open val value: String)

data class CssClass(override val value: String) : Selector(value)

data class Id(override val value: String) : Selector(value)

data class XPath(override val value: String): Selector(value) {

    companion object {

        fun link(text: String) = XPath("""//a[normalize-space() = '$text']""")

    }

}
