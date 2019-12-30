package com.kotlium

import com.kotlium.action.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import kotlin.reflect.KClass

class BrowserStage private constructor(val url: String, initActions: List<Action>) {

    private val actions = mutableListOf(*initActions.toTypedArray())

    companion object {
        operator fun invoke(url: String, browserStageConfigure: BrowserStage.() -> Unit): BrowserStage {
            return BrowserStage(url, listOf()).apply(browserStageConfigure)
        }
    }

    fun execute(driver: WebDriver): BrowserStageExecuteResult {
        actions.add(0, TransitionAction(url))

        val actionExecuteResults = mutableListOf<ActionExecuteResult>()
        var currentActionIsOk = true

        for (action in actions) {
            if (!currentActionIsOk) {
                break
            }

            val executeResult = action.execute(driver)
            currentActionIsOk = executeResult.isOk
            actionExecuteResults.add(executeResult)
        }

        actionExecuteResults += SessionCloseAction().execute(driver)

        return BrowserStageExecuteResult(url, actionExecuteResults)
    }

    fun click(by: () -> By) {
        actions += ClickAction(by())
    }

    fun input(init: InputAction.() -> Unit) {
        actions += InputAction().apply(init)
    }

    fun assertPage(assert: WebDriver.() -> Unit) {
        actions += PageAssertAction(assert)
    }

    fun <T> waitFor(condition: () -> ExpectedCondition<T>) {
        actions += WaitForAssertAction(condition())
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

    fun addLast(browserStageConfigure: BrowserStage.() -> Unit): BrowserStage {
        return BrowserStage(url, actions).apply(browserStageConfigure)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BrowserStage) return false
        if (actions != other.actions) return false
        return true
    }

    override fun hashCode(): Int {
        return actions.hashCode()
    }

}