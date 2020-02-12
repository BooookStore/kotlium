package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.action.BrowserActionType.OPERATOR
import com.kotlium.action.ClickBrowserAction
import com.kotlium.action.SessionCloseBrowserAction
import com.kotlium.action.TransitionBrowserAction
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

internal class ScenarioTest {

    @Test
    fun scenarioExecuteTest() {
        // setup and execute
        val scenarioExecuteResult = Scenario {
            browserStage("http://example1.com") {
                click { By.id("id-for-element") }
            }
            browserStage("http://example2.com") {
                click { By.id("id-for-element") }
            }
        }.execute(mockk(relaxed = true))

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
        assertThat(scenarioExecuteResult.executedStages).containsExactly(
            BrowserStageExecuteResult(
                "http://example1.com",
                listOf(
                    //@formatter:off
                    BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition http://example1.com")),
                    BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
                    //@formatter:on
                )
            ),
            BrowserStageExecuteResult(
                "http://example2.com",
                listOf(
                    //@formatter:off
                    BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition http://example2.com")),
                    BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
                    //@formatter:on
                )
            )
        )
    }

    @Test
    fun scenarioExecuteFailedTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)
        every { mockDriver.findElement(By.id("failed")) } throws NoSuchElementException()

        // execute
        val scenarioExecuteResult = Scenario {
            browserStage("http://example1.com") {
                click { By.id("id-for-element") }
            }
            browserStage("http://example2.com") {
                click { By.id("failed") }
            }
            browserStage("http://example3.com") {
                click { By.id("id-for-element") }
            }
        }.execute(mockDriver)

        // verify
        assertThat(scenarioExecuteResult.isOk).isFalse()
        assertThat(scenarioExecuteResult.executedStages).containsExactly(
            BrowserStageExecuteResult(
                "http://example1.com",
                listOf(
                    //@formatter:off
                    BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition http://example1.com")),
                    BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
                    BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
                    //@formatter:on
                )
            ),
            BrowserStageExecuteResult(
                "http://example2.com",
                listOf(
                    //@formatter:off
                    BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition http://example2.com")),
                    BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, false, listOf("click failed By.id: failed")),
                    BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
                    //@formatter:on
                )
            )
        )
    }

}
