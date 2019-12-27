package com.kotlium

interface IWebDriverWrapper {

    fun click(selector: Selector): Boolean

    fun input(selector: Selector, value: String): Boolean

    fun isTextDisplay(value: String): Boolean

}
