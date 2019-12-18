package com.kotlium

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KotliumTest {

    @Test
    fun stageDslTest() {
        val stage = Stage {
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
        val executeResult = Stage {
            click { id("id-for-element") }
        }.execute()

        // verify
        assertThat(executeResult.isOk).isTrue()
        assertThat(executeResult.executedActions).hasSize(1).containsExactly(
            ActionExecuteResult(actionClass = ClickAction::class, isOk = true, message = null)
        )
    }

}
