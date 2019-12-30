package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType.OPERATOR
import com.kotlium.action.ClickAction
import com.kotlium.action.SessionCloseAction
import com.kotlium.action.TransitionAction
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.By

internal class ScenarioTest {

    @Test
    fun scenarioExecuteTest() {
        // setup and execute
        val scenarioExecuteResult = Scenario {
            browserStage("http://example.com") {
                click { By.id("id-for-element") }
                click { By.id("id-for-element") }
                click { By.id("id-for-element") }
            }
        }.execute(mockk(relaxed = true))

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
        assertThat(scenarioExecuteResult.executedStages).containsExactly(
            BrowserStageExecuteResult(
                "http://example.com",
                listOf(
                    ActionExecuteResult(TransitionAction::class, OPERATOR, true, listOf("transition http://example.com")),
                    ActionExecuteResult(ClickAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    ActionExecuteResult(ClickAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    ActionExecuteResult(ClickAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    ActionExecuteResult(SessionCloseAction::class, OPERATOR, true, listOf("close session"))
                )
            )
        )
    }

}
