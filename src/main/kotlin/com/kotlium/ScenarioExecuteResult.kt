package com.kotlium

import com.kotlium.exception.BrowserStageException
import com.kotlium.exception.ScenarioException

data class ScenarioExecuteResult(
    val executedStages: List<BrowserStageExecuteResult>,
    val _executedStages: List<StageExecuteResult>
) {

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

    val isOk: Boolean
        get() = executedStages.all { it.isOk() }

    private val causeResult: BrowserStageExecuteResult?
        get() = executedStages.firstOrNull { !it.isOk() }

}