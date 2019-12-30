package com.kotlium.action

import com.kotlium.IWebDriverWrapper

interface Action {

    fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult

}
