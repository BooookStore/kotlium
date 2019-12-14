package com.kotlium

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LearningDSL {

    @Test
    fun operatorInvoke() {
        class Invoker {
            operator fun invoke(): String = "Hello, I am operator invoke"
        }

        val invoker = Invoker()

        assertThat(invoker()).isEqualTo("Hello, I am operator invoke")
    }

    @Test
    fun operatorInvoke_lambda() {
        class Invoker {
            operator fun invoke(lambda: () -> String) = lambda()
        }

        val invoker = Invoker()

        assertThat(invoker { "Hello, I am operator and lambda" }).isEqualTo("Hello, I am operator and lambda")
    }

    @Test
    fun operatorInvoke_lambda_receiver() {
        class Receiver(val message: String = "Hello")

        class Invoker {
            operator fun invoke(receiverLambda: Receiver.() -> String) = Receiver().receiverLambda()
        }

        val invoker = Invoker()

        assertThat(invoker { "$message, I am operator, lambda and receiver" }).isEqualTo("Hello, I am operator, lambda and receiver")
    }

    @Test
    fun operatorInvoke_lambda_receiver_run() {
        class Receiver(val message: String = "Hello")

        class Invoker {
            operator fun invoke(receiverLambda: Receiver.() -> String) = Receiver().run(receiverLambda)
        }

        val invoker = Invoker()

        assertThat(invoker { "$message, I am operator, lambda, receiver and run" }).isEqualTo("Hello, I am operator, lambda, receiver and run")
    }

    @Test
    fun operatorInvoke_lambda_receiver_apply() {
        data class Receiver(var message: String = "Hello")

        class Invoker {
            operator fun invoke(receiverLambda: Receiver.() -> Unit) = Receiver().apply(receiverLambda)
        }

        val invoker = Invoker()

        assertThat(
            invoker {
                message = "$message, I am operator, lambda, receiver and run"
            }
        ).isEqualTo(Receiver("Hello, I am operator, lambda, receiver and run"))
    }

    @Test
    fun operatorInvoke_lambda_receiver_apply_method() {
        data class Invoker(private var message: String = "Default") {

            fun changeMessage(newMessage: String) {
                message = newMessage
            }

            operator fun invoke(receiverLambda: Invoker.() -> Unit) = Invoker().apply(receiverLambda)
        }

        val invoker = Invoker()

        assertThat(
            invoker {
                changeMessage("Hello, I am operator, lambda, receiver and run")
            }
        ).isEqualTo(Invoker("Hello, I am operator, lambda, receiver and run"))
    }

    @Test
    fun extensionFunction_lambda() {
        data class Child(private var message: String = "") {
            fun changeMessage(newMessage: String) {
                message = newMessage
            }

            fun printMessage() = println(message)
        }

        data class ParentInvoker(private val childList: MutableList<Child> = mutableListOf()) {
            operator fun invoke(lambda: Child.() -> Unit) {
                childList += Child().apply(lambda)
            }
        }

        val parentInvoker = ParentInvoker()
        parentInvoker {
            changeMessage("Hello, 1!")
            printMessage()
        }
        parentInvoker {
            changeMessage("Hello, 2!")
            printMessage()
        }

        assertThat(parentInvoker).isEqualTo(
            ParentInvoker(
                mutableListOf(
                    Child("Hello, 1!"),
                    Child("Hello, 2!")
                )
            )
        )
    }

}