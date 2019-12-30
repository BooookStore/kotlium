package com.kotlium

import org.openqa.selenium.WebDriver

class Scenario {

    private val stages = mutableListOf<BrowserStage>()

    companion object {

        operator fun invoke(scenarioConfigure: Scenario.() -> Unit): Scenario {
            return Scenario().apply(scenarioConfigure)
        }

    }

    fun browserStage(url: String, browserStageConfigure: BrowserStage.() -> Unit) {
        stages += BrowserStage(url, browserStageConfigure)
    }

    fun execute(webDriver: WebDriver): ScenarioExecuteResult {
        val stageExecuteResult = mutableListOf<BrowserStageExecuteResult>()
        var currentStageIsOk = true

        for (stage in stages) {
            if (!currentStageIsOk) {
                break
            }

            val executeResult = stage.execute(webDriver)
            currentStageIsOk = executeResult.isOk
            stageExecuteResult += executeResult
        }

        return ScenarioExecuteResult(stageExecuteResult)
    }

}