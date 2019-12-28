package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType.OPERATOR
import com.kotlium.action.ClickAction
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BrowserStageTest {

    private val config: BrowserStageConfiguration = BrowserStageConfiguration(
        name = "customer page",
        url = "http://kotlium.example/customer"
    )

    @Test
    fun stageExecuteResultTest() {
        // setup
        val allOkIWebDriverWrapper: IWebDriverWrapper = mockk()
        every { allOkIWebDriverWrapper.get(any()) } returns true
        every { allOkIWebDriverWrapper.click(any()) } returns true
        every { allOkIWebDriverWrapper.input(any(), any(), any()) } returns true
        every { allOkIWebDriverWrapper.deleteSession() } returns Unit

        // execute
        val executeResult = BrowserStage(config, allOkIWebDriverWrapper) {
            click { target = Id("id-for-element") }
            click { target = Id("id-for-element") }
        }.execute()

        // verify
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click by id. value is id-for-element")),
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click by id. value is id-for-element"))
        )

    }

    @Test
    fun stageExecuteFailedTest() {
        // setup
        val mockIWebDriverWrapper = mockk<IWebDriverWrapper>()
        every { mockIWebDriverWrapper.get(any()) } returns true
        every { mockIWebDriverWrapper.click(any()) } returns true
        every { mockIWebDriverWrapper.click(Id("failed-id")) } returns false
        every { mockIWebDriverWrapper.deleteSession() } returns Unit

        // execute
        val executeResult = BrowserStage(config, mockIWebDriverWrapper) {
            click { target = Id("success-id") }
            click { target = Id("failed-id") }
            click { target = Id("success-id") }
        }.execute()

        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click by id. value is success-id")),
            ActionExecuteResult(ClickAction::class, false, OPERATOR, listOf("click by id. value is failed-id"))
        )
    }

}
