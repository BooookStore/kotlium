package com.kotlium

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StageTest {

    private val config: BrowserStageConfiguration = BrowserStageConfiguration(
        name = "customer page",
        url = "http://kotlium.example/customer"
    )

    @Test
    fun stageDslTest() {
        val stage = BrowserStage(config) {
            click {
                text("Button1")
            }
            click {
                `class`("Button2")
            }
            click {
                id("Button3")
            }
            input {
                id("message")
                value("Hello")
            }
            InputCard::class {
                cvv = "xxx-xxx-xxx"
            }
            ClickRegisterCard::class()
            assertPage {
                text { "Complete" } display true
                text { "Error" } display false
            }
        }

        assertThat(stage.actions()).containsExactlyInAnyOrder(
            ClickAction().apply { target = Text("Button1") },
            ClickAction().apply { target = Class("Button2") },
            ClickAction().apply { target = Id("Button3") },
            InputAction().apply {
                id("message")
                value("Hello")
            },
            InputCard().apply {
                cvv = "xxx-xxx-xxx"
            },
            ClickRegisterCard(),
            PageAssertAction(
                mutableListOf(
                    PageAssertAction.TextAssertion("Complete", true),
                    PageAssertAction.TextAssertion("Error", false)
                )
            )
        )
    }

    @Test
    fun stageExecuteTest() {
        // setup
        val executeResult = BrowserStage(config) {
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

}
