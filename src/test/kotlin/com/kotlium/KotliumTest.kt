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
                target = "form1"
                value = "hello"
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
                target = "form1"
                value = "hello"
            },
            InputCard().apply {
                cvv = "xxx-xxx-xxx"
            }
        )
    }

}
