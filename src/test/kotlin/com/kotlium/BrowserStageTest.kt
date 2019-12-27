package com.kotlium

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
        allOkIWebDriverWrapper = mockk<IWebDriverWrapper>()
        every { allOkIWebDriverWrapper.click(any()) } returns true
        every { allOkIWebDriverWrapper.input(any(), any()) } returns true
    }

    @Test
    fun stageDslTest() {
        val stage = BrowserStage(config, allOkIWebDriverWrapper) {
            click {
                cssClass("Button2")
            }
            click {
                id("Button3")
            }
            input {
                id("message")
                value("This is input value")
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
                id("message")
                value("This is input value")
            },
            InputCard().apply {
                cvv = "xxx-xxx-xxx"
            },
            ClickRegisterCard(),
            PageAssertAction(
                mutableListOf(
                    PageAssertAction.TextAssertion("Complete", true),
                    PageAssertAction.TextAssertion("Go back to home", true),
                    PageAssertAction.TextAssertion("Error", false)
                )
            )
        )
    }

    @Test
    fun stageExecuteResultTest() {
        // setup
        val executeResult = BrowserStage(config, allOkIWebDriverWrapper) {
            click { id("id-for-element") }
            click { id("id-for-element") }
        }.execute()

        // verify
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, "click by id. value is id-for-element"),
            ActionExecuteResult(ClickAction::class, true, "click by id. value is id-for-element")
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
            click { id("success-id") }
            click { id("failed-id") }
            click { id("success-id") }
        }.execute()

        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.executedActions).hasSize(2).containsExactly(
            ActionExecuteResult(ClickAction::class, true, "click by id. value is success-id"),
            ActionExecuteResult(ClickAction::class, false, "click by id. value is failed-id")
        )
    }

}
