package com.kotlium

import com.kotlium.action.ActionExecuteResult
import com.kotlium.action.ActionType.OPERATOR
import com.kotlium.action.ClickAction
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.id
import org.openqa.selenium.WebDriver

internal class BrowserStageTest {

    private val config: BrowserStageConfiguration = BrowserStageConfiguration(
        name = "customer page",
        url = "http://kotlium.example/customer"
    )

    @Test
    fun stageExecuteTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)

        // execute
        val executeResult = BrowserStage {
            click(id("id-for-element"))
            click(id("id-for-element"))
        }.execute(config, mockDriver)

        // verify
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: id-for-element")),
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: id-for-element"))
        )
    }

    @Test
    fun stageExecuteFailedTest() {
        // setup
        val mockDriver: WebDriver = mockk(relaxed = true)
        every { mockDriver.findElement(id("failed-id")) } throws NoSuchElementException()

        // execute
        val executeResult = BrowserStage {
            click(id("success-id"))
            click(id("failed-id"))
            click(id("success-id"))
        }.execute(config, mockDriver)

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, OPERATOR, listOf("click By.id: success-id")),
            ActionExecuteResult(ClickAction::class, false, OPERATOR, listOf("click By.id: failed-id"))
        )
    }

}
