package com.kotlium

import com.kotlium.exception.BrowserStageException
import com.kotlium.exception.ScenarioException

data class ScenarioExecuteResult(val executedStages: List<BrowserStageExecuteResult>) {

    fun throwIfFailed() {
        kotlin.runCatching {
            causeResult?.throwIfFailed()
        }.onFailure { exception ->
            if (exception is BrowserStageException) {
                throw ScenarioException("Scenario execute failed", exception)
            } else {
                throw IllegalStateException()
            }
        }
    }

    val causeResult: BrowserStageExecuteResult?
        get() = executedStages.firstOrNull { !it.isOk }

    val isOk: Boolean
        get() = executedStages.all { it.isOk }
}