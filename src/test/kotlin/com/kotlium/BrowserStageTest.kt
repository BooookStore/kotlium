package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType.OPERATOR
import com.kotlium.action.ClickBrowserAction
import com.kotlium.action.SessionCloseBrowserAction
import com.kotlium.action.TransitionBrowserAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.url).isEqualTo(url)
        assertThat(executeResult.executedActions).containsExactly(
            ActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition $url")),
            ActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
            ActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: id-for-element")),
            ActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
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
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedActions).containsExactly(
            ActionExecuteResult(TransitionBrowserAction::class, OPERATOR, true, listOf("transition $url")),
            ActionExecuteResult(ClickBrowserAction::class, OPERATOR, true, listOf("click By.id: success-id")),
            ActionExecuteResult(ClickBrowserAction::class, OPERATOR, false, listOf("click failed By.id: failed-id")),
            ActionExecuteResult(SessionCloseBrowserAction::class, OPERATOR, true, listOf("close session"))
        )
        verify(exactly = 1) { mockDriver.get(url) }
        verify(exactly = 1) { mockDriver.close() }
    }

}
