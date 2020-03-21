package com.kotlium

import org.openqa.selenium.WebDriver

abstract class Stage {

    abstract fun execute(driver: WebDriver): StageExecuteResult

}