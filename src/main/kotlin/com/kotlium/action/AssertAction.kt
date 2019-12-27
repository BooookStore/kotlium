package com.kotlium.action

import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.ASSERT

class PageAssertAction(vararg initAssertions: Assertion) : Action {

    private val assertions: MutableList<Assertion> = mutableListOf(*initAssertions)

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val assertResults = assertions.map { it.assert(iWebDriverWrapper) }
        return ActionExecuteResult(
            actionClass = PageAssertAction::class,
            isOk = assertResults.all { it.isOk },
            type = ASSERT,
            message = assertResults.mapNotNull { it.message }
        )
    }

    fun text(textConfigure: () -> String): TextAssertion {
        return TextAssertion(textConfigure())
    }

    infix fun TextAssertion.display(expect: Boolean) {
        this.expect = expect
        assertions += this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PageAssertAction) return false
        if (assertions != other.assertions) return false
        return true
    }

    override fun hashCode(): Int = assertions.hashCode()

}

interface Assertion {

    fun assert(iWebDriverWrapper: IWebDriverWrapper): AssertionResult

}

data class AssertionResult(val isOk: Boolean, val message: String?)

data class TextAssertion(var text: String, var expect: Boolean? = null) : Assertion {

    override fun assert(iWebDriverWrapper: IWebDriverWrapper): AssertionResult {
        val notNullExpect = checkNotNull(expect) { "expect is null. please set expect." }
        val isDisplay = iWebDriverWrapper.isTextDisplay(text)

        val isOk = isDisplay == notNullExpect

        return if (isOk) {
            AssertionResult(true, null)
        } else {
            AssertionResult(false, "'$text' is not display")
        }
    }

}
