package com.kotlium

import com.kotlium.action.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import kotlin.reflect.KClass

class BrowserStage(val config: BrowserStageConfiguration, val iWebDriverWrapper: IWebDriverWrapper) {

    private val actions = mutableListOf<Action>()

    fun actions(): List<Action> = actions

    companion object {
        operator fun invoke(
            config: BrowserStageConfiguration,
            iWebDriverWrapper: IWebDriverWrapper,
            browserStageConfigure: BrowserStage.() -> Unit
        ): BrowserStage {
            return BrowserStage(config, iWebDriverWrapper).apply(browserStageConfigure)
        }
    }

    fun execute(): StageExecuteResult {
        check(iWebDriverWrapper.get(config.url)) { "can't access url" }

        val actionExecuteResults = mutableListOf<ActionExecuteResult>()
        var currentActionIsOk = true

        for (action in actions) {
            if (!currentActionIsOk) {
                break
            }

            val executeResult = action.execute(iWebDriverWrapper)
            currentActionIsOk = executeResult.isOk
            actionExecuteResults.add(executeResult)
        }

        iWebDriverWrapper.deleteSession()

        return StageExecuteResult(actionExecuteResults)
    }

    fun click(by: By) {
        actions += ClickAction(by)
    }

    fun input(init: InputAction.() -> Unit) {
        actions += InputAction().apply(init)
    }

    fun assertPage(assert: WebDriver.() -> Unit) {
        actions += PageAssertAction(assert)
    }

    fun <T> waitFor(condition: ExpectedCondition<T>) {
        actions += WaitForAssertAction(condition)
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

    fun addLast(browserStageConfigure: BrowserStage.() -> Unit) {
        this.browserStageConfigure()
    }

}