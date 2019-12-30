package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType
import com.kotlium.action.ClickAction
import org.openqa.selenium.WebDriver

class Scenario {

    companion object {

        operator fun invoke(scenarioConfigure: Scenario.() -> Unit): Scenario {
            return Scenario()
        }

    }

    fun browserStage(url: String, browserStageConfigure: BrowserStage.() -> Unit) {

    }

    fun execute(webDriver: WebDriver): ScenarioExecuteResult {
        return ScenarioExecuteResult(
            listOf(
                BrowserStageExecuteResult(
                    "http://example.com",
                    listOf(
                        ActionExecuteResult(
                            ClickAction::class,
                            ActionType.OPERATOR,
                            true,
                            listOf("click By.id: id-for-element")
                        ),
                        ActionExecuteResult(
                            ClickAction::class,
                            ActionType.OPERATOR,
                            true,
                            listOf("click By.id: id-for-element")
                        ),
                        ActionExecuteResult(
                            ClickAction::class,
                            ActionType.OPERATOR,
                            true,
                            listOf("click By.id: id-for-element")
                        )
                    )
                )
            )
        )
    }

}