package com.kotlium

interface IWebDriverWrapper {

    fun click(selector: Selector): Boolean

    fun input(selector: Selector, value: String, lastEnter: Boolean): Boolean

    fun isTextDisplay(value: String): Boolean

    fun get(url: String): Boolean

    fun currentUrl(): String

    fun deleteSession()

}
