package com.kotlium

import com.kotlium.action.ActionExecuteResult

data class BrowserStageExecuteResult(val executedActions: List<ActionExecuteResult>) {
    val isOk: Boolean
        get() = executedActions.all { it.isOk }
}
