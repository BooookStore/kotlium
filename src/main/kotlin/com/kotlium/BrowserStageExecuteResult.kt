package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult

data class BrowserStageExecuteResult(val url: String, val executedBrowserActions: List<BrowserActionExecuteResult>) {
    val isOk: Boolean
        get() = executedBrowserActions.all { it.isOk }
}
