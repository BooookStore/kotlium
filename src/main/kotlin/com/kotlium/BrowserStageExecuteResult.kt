package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult

data class BrowserStageExecuteResult(val url: String, val executedBrowserActions: List<BrowserActionExecuteResult>) {

    fun throwIfFailed() {

    }

    val isOk: Boolean
        get() = executedBrowserActions.all { it.isOk }
}
