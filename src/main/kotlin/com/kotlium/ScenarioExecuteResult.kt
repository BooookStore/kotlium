package com.kotlium

data class ScenarioExecuteResult(val executedStages: List<BrowserStageExecuteResult>) {
    val isOk: Boolean
        get() = executedStages.all { it.isOk }
}