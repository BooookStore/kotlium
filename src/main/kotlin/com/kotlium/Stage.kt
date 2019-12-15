package com.kotlium

import kotlin.reflect.KClass

class Stage {

    private val actions = mutableListOf<Action>()

    fun actions(): List<Action> = actions

    companion object {
        operator fun invoke(stageConfigure: Stage.() -> Unit): Stage {
            return Stage().apply(stageConfigure)
        }
    }

    fun click(init: ClickAction.() -> Unit) {
        actions += ClickAction().apply(init)
    }

    fun input(init: InputAction.() -> Unit) {
        actions += InputAction().apply(init)
    }

    fun assertPage(init: PageAssertAction.() -> Unit) {
        actions += PageAssertAction().apply(init)
    }

    operator fun <T : Action> T.invoke(init: T.() -> Unit) {
        val kClass = this::class
        val action = kClass.java.newInstance()
        actions += action.apply(init)
    }

    operator fun <T : Action> KClass<T>.invoke(init: T.() -> Unit) {
        val action = this.java.newInstance()
        actions += action.apply(init)
    }

    operator fun <T : Action> KClass<T>.invoke() {
        actions += this.java.newInstance()
    }

}