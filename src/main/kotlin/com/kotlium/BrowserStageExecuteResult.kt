package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.exception.KotliumException

data class BrowserStageExecuteResult(val url: String, val executedBrowserActions: List<BrowserActionExecuteResult>) {

    fun throwIfFailed() {
        if (!isOk) throw KotliumException()
    }

    val isOk: Boolean
        get() = executedBrowserActions.all { it.isOk }
}
