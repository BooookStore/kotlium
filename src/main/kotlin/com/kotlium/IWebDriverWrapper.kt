package com.kotlium

interface IWebDriverWrapper {

    fun click(selector: String): Boolean

    fun input(selector: String, value: String): Boolean

}
