package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.action.BrowserActionType.OPERATOR
import com.kotlium.action.ClickBrowserAction
import com.kotlium.action.SessionCloseBrowserAction
import com.kotlium.action.TransitionBrowserAction
import com.kotlium.exception.BrowserStageException
import com.kotlium.exception.ScenarioException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.urlToBe

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
        catchThrowable {
            scenarioExecuteResult.throwIfFailed()
        }.let { exception ->
            assertThat(exception).isNull()
        }
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
        catchThrowable {
            scenarioExecuteResult.throwIfFailed()
        }.let { exception ->
            //@formatter:off
            assertThat(exception)
                .isNotNull()
                .isExactlyInstanceOf(ScenarioException::class.java)
                .hasMessage("Scenario execute failed")
                .hasCause(BrowserStageException.from(BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, false, listOf("click failed By.id: failed"))))
            //@formatter:on
        }
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

    @Suppress("UsePropertyAccessSyntax")
    @Test
    fun scenarioCanTakeCustomStageTest() {
        // setup
        // define custom stage class
        class CustomStage(val argument: String) : Stage() {
            override fun execute(): StageExecuteResult {
                return StageExecuteResult(isOk = true)
            }
        }

        // define custom stage register function
        fun Scenario.customStage(argument: String) {
            val customStage = CustomStage(argument)
            addLast(customStage)
        }

        // execute
        val scenarioExecuteResult = Scenario {
            customStage("stageArgument")
            customStage("stageArgument")
        }.execute(mockk(relaxed = true))

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
        assertThat(scenarioExecuteResult._executedStages).containsExactly(
            StageExecuteResult(isOk = true),
            StageExecuteResult(isOk = true)
        )
    }

    @Test
    fun scenarioExecuteMultipleActionTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)
        every { mockDriver.currentUrl } returns "http://example.com/user"
        every { mockDriver.findElement(any()) } returns mockk(relaxed = true) {
            every { isDisplayed } returns true
        }

        // execute
        Scenario {
            browserStage("http://example.com") {
                click {
                    By.id("login")
                }
                input {
                    target = By.xpath("//input[@class='user-name']")
                    value = "USER-NAME"
                }
                input {
                    target = By.xpath("//input[@class='password']")
                    value = "PASSWORD"
                    lastEnter = true
                }
                waitFor {
                    urlToBe("http://example.com/user")
                }
                assertPage {
                    assertThat(findElement(By.id("user-name")).isDisplayed).isTrue()
                }
            }
        }.execute(mockDriver).throwIfFailed()
    }

}
