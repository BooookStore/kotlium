package com.kotlium

import org.openqa.selenium.By

interface IWebDriverWrapper {

    fun click(by: By): Boolean

    fun input(by: By, value: String, lastEnter: Boolean): Boolean

    fun isTextDisplay(value: String): Boolean

    fun get(url: String): Boolean

    fun currentUrl(): String

    fun deleteSession()

}
