package com.kotlium

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

interface IWebDriverWrapper {

    val driver: WebDriver

    fun click(by: By): Boolean

    fun input(by: By, value: String, lastEnter: Boolean): Boolean

    fun get(url: String): Boolean

    fun currentUrl(): String

    fun deleteSession()

}
