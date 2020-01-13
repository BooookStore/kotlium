package com.kotlium

import com.kotlium.action.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class BrowserStage private constructor(private val url: String, initBrowserActions: List<BrowserAction>) {

    private val logger = LoggerFactory.getLogger(BrowserStage::class.java)

    private val actions = mutableListOf(*initBrowserActions.toTypedArray())

    companion object {
        operator fun invoke(url: String, browserStageConfigure: BrowserStage.() -> Unit): BrowserStage {
            return BrowserStage(url, listOf()).apply(browserStageConfigure)
        }
    }

    fun execute(driver: WebDriver): BrowserStageExecuteResult {
        logger.info("start browser stage on {}", url)

        actions.add(0, TransitionBrowserAction(url))
        val actionExecuteResults = executeAllAction(driver)
        val result = actionExecuteResults + SessionCloseBrowserAction().execute(driver)

        logger.info("end browser stage on {}", url)

        return BrowserStageExecuteResult(url, result)
    }

    private fun executeAllAction(driver: WebDriver): List<ActionExecuteResult> =
        actions.fold(mutableListOf()) { result, action ->
            result.add(action.execute(driver))
            if (result.last().isOk) return@fold result else return result
        }

    fun click(by: () -> By) {
        actions += ClickBrowserAction(by())
    }

    fun input(init: InputBrowserAction.() -> Unit) {
        actions += InputBrowserAction().apply(init)
    }

    fun assertPage(assert: WebDriver.() -> Unit) {
        actions += PageAssertBrowserAction(assert)
    }

    fun <T> waitFor(condition: () -> ExpectedCondition<T>) {
        actions += WaitForAssertBrowserAction(condition())
    }

    operator fun <T : BrowserAction> T.invoke(init: T.() -> Unit) {
        val kClass = this::class
        val action = kClass.java.newInstance()
        actions += action.apply(init)
    }

    operator fun <T : BrowserAction> KClass<T>.invoke(init: T.() -> Unit) {
        val action = this.java.newInstance()
        actions += action.apply(init)
    }

    operator fun <T : BrowserAction> KClass<T>.invoke() {
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