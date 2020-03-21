package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.exception.BrowserStageException

data class BrowserStageExecuteResult(
    val url: String,
    val executedBrowserActions: List<BrowserActionExecuteResult>
) : StageExecuteResult() {

    override fun throwIfFailed() {
        causedResult?.let { throw BrowserStageException.from(it) }
    }

    override fun isOk(): Boolean {
        return executedBrowserActions.all { it.isOk }
    }

    private val causedResult: BrowserActionExecuteResult?
        get() = executedBrowserActions.firstOrNull { !it.isOk }

}
