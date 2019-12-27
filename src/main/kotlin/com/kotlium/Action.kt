package com.kotlium

interface Action {

    fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult

}