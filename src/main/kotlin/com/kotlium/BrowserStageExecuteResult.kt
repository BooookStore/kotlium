package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.exception.BrowserStageException

data class BrowserStageExecuteResult(val url: String, val executedBrowserActions: List<BrowserActionExecuteResult>) {

    fun throwIfFailed() {
        causedResult?.let { throw BrowserStageException.from(it) }
    }

    val isOk: Boolean
        get() = executedBrowserActions.all { it.isOk }

    private val causedResult: BrowserActionExecuteResult?
        get() = executedBrowserActions.firstOrNull { !it.isOk }

}
