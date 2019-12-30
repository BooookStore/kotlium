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
        return ScenarioExecuteResult(listOf(stages[0].execute(webDriver)))
    }

}