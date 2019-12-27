package com.kotlium.action

import com.kotlium.ActionExecuteResult
import com.kotlium.IWebDriverWrapper

interface Action {

    fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult

}

enum class ActionType { OPERATOR, ASSERT }