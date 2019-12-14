package com.kotlium

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KotliumTest {

    @Test
    fun dslTest() {
        val stage = Stage {
            click {
                target = "Button1"
            }
            click {
                target = "Button2"
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
            ClickAction().apply { target = "Button1" },
            ClickAction().apply { target = "Button2" },
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
