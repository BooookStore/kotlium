package com.kotlium

import com.kotlium.LearningDSL_operatorInvoke_companionObject.InputCards.Companion.inputCards
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LearningDSL_operatorInvoke_companionObject {

    interface Action {

        var target: String

    }

    interface CustomAction : Action

    data class ClickAction(override var target: String = "") : Action
    data class InputCards(override var target: String = "") : CustomAction {

        companion object {
            fun inputCards(lamb: InputCards.() -> Unit): InputCards {
                return InputCards().apply(lamb)
            }
        }

    }

    data class Scenario(val actionList: MutableList<Action> = mutableListOf()) {

        fun click(init: Action.() -> Unit) {
            actionList.add(ClickAction().apply(init))
        }

        fun domain(actionGenerator: () -> Action) {
            actionList.add(actionGenerator())
        }

        companion object {
            operator fun invoke(lambda: Scenario.() -> Unit): Scenario {
                return Scenario().apply(lambda)
            }
        }

    }

    @Test
    fun operatorFunction_companionObject() {
        val parentInvoker = Scenario {
            click {
                target = "Hello, 1!"
            }
            click {
                target = "Hello, 2!"
            }
            domain {
                inputCards {
                    target = "Custom"
                }
            }
        }

        Assertions.assertThat(parentInvoker).isEqualTo(
            Scenario(
                mutableListOf(
                    ClickAction("Hello, 1!"),
                    ClickAction("Hello, 2!"),
                    InputCards("Custom")
                )
            )
        )
    }


}