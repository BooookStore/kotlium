package com.kotlium

import com.kotlium.action.BrowserActionExecuteResult
import com.kotlium.action.BrowserActionType.OPERATOR
import com.kotlium.action.ClickBrowserAction
import com.kotlium.action.SessionCloseBrowserAction
import com.kotlium.action.TransitionBrowserAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.id
import org.openqa.selenium.WebDriver

internal class BrowserStageTest {

    private val url = "http://kotlium.example/customer"

    @Test
    fun stageExecuteTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)

        // execute
        val executeResult = BrowserStage(url) {
            click { id("id-for-element") }
            click { id("id-for-element") }
        }.execute(mockDriver)

        // verify
        catchThrowable {
            executeResult.throwIfFailed()
        }.let { exception ->
            assertThat(exception).isNull()
        }
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.url).isEqualTo(url)
        assertThat(executeResult.executedBrowserActions).containsExactly(
            BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition $url")),
            BrowserActionExecuteResult(
                ClickBrowserAction::class,
                OPERATOR,
                true,
                listOf("click By.id: id-for-element")
            ),
            BrowserActionExecuteResult(
                ClickBrowserAction::class,
                OPERATOR,
                true,
                listOf("click By.id: id-for-element")
            ),
            BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
        )
        verify(exactly = 1) { mockDriver.get(url) }
        verify(exactly = 1) { mockDriver.close() }
    }

    @Test
    fun stageExecuteFailedTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)
        every { mockDriver.findElement(id("failed-id")) } throws NoSuchElementException()

        // execute
        val executeResult = BrowserStage(url) {
            click { id("success-id") }
            click { id("failed-id") }
            click { id("success-id") }
        }.execute(mockDriver)

        // verify
        assertThatThrownBy {
            executeResult.throwIfFailed()
        }
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedBrowserActions).containsExactly(
            BrowserActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition $url")),
            BrowserActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: success-id")),
            BrowserActionExecuteResult(
                ClickBrowserAction::class,
                OPERATOR,
                false,
                listOf("click failed By.id: failed-id")
            ),
            BrowserActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
        )
        verify(exactly = 1) { mockDriver.get(url) }
        verify(exactly = 1) { mockDriver.close() }
    }

}
