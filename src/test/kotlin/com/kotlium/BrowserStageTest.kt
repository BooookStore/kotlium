package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType.OPERATOR
import com.kotlium.action.ClickAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.id

internal class BrowserStageTest {

    private val config: BrowserStageConfiguration = BrowserStageConfiguration(
        name = "customer page",
        url = "http://kotlium.example/customer"
    )

    @Test
    fun stageExecuteTest() {
        // setup
        val allOkIWebDriverWrapper: IWebDriverWrapper = mockk()
        every { allOkIWebDriverWrapper.get(any()) } returns true
        every { allOkIWebDriverWrapper.click(any()) } returns true
        every { allOkIWebDriverWrapper.deleteSession() } returns Unit

        // execute
        val executeResult = BrowserStage(config, allOkIWebDriverWrapper) {
            click(id("id-for-element"))
            click(id("id-for-element"))
        }.execute()

        // verify
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: id-for-element")),
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: id-for-element"))
        )
        verify(exactly = 1) { allOkIWebDriverWrapper.get(config.url) }
        verify(exactly = 2) { allOkIWebDriverWrapper.click(id("id-for-element")) }
        verify(exactly = 1) { allOkIWebDriverWrapper.deleteSession() }
    }

    @Test
    fun stageExecuteFailedTest() {
        // setup
        val mockIWebDriverWrapper: IWebDriverWrapper = mockk()
        every { mockIWebDriverWrapper.get(any()) } returns true
        every { mockIWebDriverWrapper.click(any()) } returns true
        every { mockIWebDriverWrapper.click(id("failed-id")) } returns false
        every { mockIWebDriverWrapper.deleteSession() } returns Unit

        // execute
        val executeResult = BrowserStage(config, mockIWebDriverWrapper) {
            click(id("success-id"))
            click(id("failed-id"))
            click(id("success-id"))
        }.execute()

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: success-id")),
            ActionExecuteResult(ClickAction::class, false, OPERATOR, listOf("click By.id: failed-id"))
        )
        verify(exactly = 1) { mockIWebDriverWrapper.click(id("success-id")) }
    }

}
