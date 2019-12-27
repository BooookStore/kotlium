package com.kotlium.action

import com.kotlium.ActionExecuteResult
import com.kotlium.IWebDriverWrapper
import com.kotlium.action.ActionType.ASSERT

interface AssertAction : Action

class PageAssertAction(vararg initAssertions: Assertion) :
    AssertAction {

    private val assertions: MutableList<Assertion> = mutableListOf(*initAssertions)

    override fun execute(iWebDriverWrapper: IWebDriverWrapper): ActionExecuteResult {
        val isOk = assertions.all { it.assert(iWebDriverWrapper) }
        return ActionExecuteResult(
            PageAssertAction::class,
            isOk,
            ASSERT,
            "'!' is not display"
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

    fun assert(iWebDriverWrapper: IWebDriverWrapper): Boolean

}

data class TextAssertion(var text: String, var expect: Boolean? = null) :
    Assertion {

    override fun assert(iWebDriverWrapper: IWebDriverWrapper): Boolean = iWebDriverWrapper.isTextDisplay(text)

}
