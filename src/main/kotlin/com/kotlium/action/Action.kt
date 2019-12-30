package com.kotlium.action

import org.openqa.selenium.WebDriver

interface Action {

    fun execute(webDriver: WebDriver): ActionExecuteResult

}
