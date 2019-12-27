package com.kotlium

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

        return StageExecuteResult(actionExecuteResults)
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