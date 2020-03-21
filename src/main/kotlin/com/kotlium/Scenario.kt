package com.kotlium

import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory

class Scenario {

    private val logger = LoggerFactory.getLogger(Scenario::class.java)

    private val stages = mutableListOf<Stage>()

    private val _stages = mutableListOf<Stage>()

    companion object {

        operator fun invoke(scenarioConfigure: Scenario.() -> Unit): Scenario {
            return Scenario().apply(scenarioConfigure)
        }

    }

    fun browserStage(url: String, browserStageConfigure: BrowserStage.() -> Unit) {
        stages += BrowserStage(url, browserStageConfigure)
    }

    fun execute(webDriver: WebDriver): ScenarioExecuteResult {
        val _stageExecuteResult = executeAllStage(webDriver)
        return ScenarioExecuteResult(executeAllAction(webDriver) + _stageExecuteResult)
    }

    fun addLast(stage: Stage) {
        _stages.add(stage)
    }

    private fun executeAllStage(webDriver: WebDriver): List<StageExecuteResult> {
        return _stages.fold(mutableListOf()) { result, stage ->
            result.add(stage.execute(webDriver))
            if (result.last().isOk()) return@fold result else return result
        }
    }

    private fun executeAllAction(webDriver: WebDriver): List<StageExecuteResult> {
        logger.info("start scenario")

        val result: List<StageExecuteResult> = stages.fold(mutableListOf()) { result, stage ->
            result.add(stage.execute(webDriver))
            if (result.last().isOk()) return@fold result else return result
        }

        logger.info("end scenario")

        return result
    }

}