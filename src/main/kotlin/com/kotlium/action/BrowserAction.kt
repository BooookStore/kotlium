package com.kotlium.action

import org.openqa.selenium.WebDriver

interface BrowserAction {

    fun execute(webDriver: WebDriver): ActionExecuteResult

}
