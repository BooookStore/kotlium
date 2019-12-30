package com.kotlium

import com.kotlium.action.ActionExecuteResult

data class BrowserStageExecuteResult(val url: String, val executedActions: List<ActionExecuteResult>) {
    val isOk: Boolean
        get() = executedActions.all { it.isOk }
}
