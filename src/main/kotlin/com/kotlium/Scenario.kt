package com.kotlium

import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory

class Scenario {

    private val logger = LoggerFactory.getLogger(Scenario::class.java)

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
        return ScenarioExecuteResult(executeAllAction(webDriver))
    }

    private fun executeAllAction(webDriver: WebDriver): List<BrowserStageExecuteResult> {
        logger.info("start scenario")

        val result: List<BrowserStageExecuteResult> = stages.fold(mutableListOf()) { result, stage ->
            result.add(stage.execute(webDriver))
            if (result.last().isOk) return@fold result else return result
        }

        logger.info("end scenario")

        return result
    }

}