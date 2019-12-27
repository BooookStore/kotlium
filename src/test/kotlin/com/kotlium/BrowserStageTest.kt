package com.kotlium

import com.kotlium.action.*
import com.kotlium.action.ActionType.OPERATOR
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BrowserStageTest {

    private val config: BrowserStageConfiguration = BrowserStageConfiguration(
        name = "customer page",
        url = "http://kotlium.example/customer"
    )

    private lateinit var allOkIWebDriverWrapper: IWebDriverWrapper

    @BeforeEach
    fun beforeEach() {
        allOkIWebDriverWrapper = mockk()
        every { allOkIWebDriverWrapper.click(any()) } returns true
        every { allOkIWebDriverWrapper.input(any(), any()) } returns true
    }

    @Test
    fun stageDslTest() {
        val stage = BrowserStage(config, allOkIWebDriverWrapper) {
            click {
                target = CssClass("Button2")
            }
            click {
                target = Id("Button3")
            }
            input {
                target = Id("message")
                value = "This is input value"
            }
            InputCard::class {
                cvv = "xxx-xxx-xxx"
            }
            ClickRegisterCard::class()
            assertPage {
                text { "Complete" } display true
                text { "Go back to home" } display true
                text { "Error" } display false
            }
        }

        assertThat(stage.actions()).containsExactlyInAnyOrder(
            ClickAction().apply { target = CssClass("Button2") },
            ClickAction().apply { target = Id("Button3") },
            InputAction().apply {
                target = Id("message")
                value = "This is input value"
            },
            InputCard().apply {
                cvv = "xxx-xxx-xxx"
            },
            ClickRegisterCard(),
            PageAssertAction(
                TextAssertion("Complete", true),
                TextAssertion("Go back to home", true),
                TextAssertion("Error", false)
            )
        )
    }

    @Test
    fun stageExecuteResultTest() {
        // setup
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
        every { mockIWebDriverWrapper.click(any()) } returns true
        every { mockIWebDriverWrapper.click(Id("failed-id")) } returns false

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
