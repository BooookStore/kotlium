package com.kotlium

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KotliumTest {

    @Test
    fun dslTest() {
        val stage = Stage {
            click {
                text { "Button1" }
            }
            click {
                `class` { "Button2" }
            }
            click {
                id { "Button3" }
            }
            input {
                id { "message" }
                value { "Hello!" }
            }
            InputCard::class {
                cvv = "xxx-xxx-xxx"
            }
        }

        assertThat(stage.actions()).containsExactlyInAnyOrder(
            ClickAction().apply { target = Text("Button1") },
            ClickAction().apply { target = Class("Button2") },
            ClickAction().apply { target = Id("Button3") },
            InputAction().apply {
                id { "message" }
                value { "Hello!" }
            },
            InputCard().apply {
                cvv = "xxx-xxx-xxx"
            }
        )
    }

}
