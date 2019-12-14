package com.kotlium

interface AssertAction : Action

interface Assertion

data class PageAssertAction(private val assertions: MutableList<Assertion> = mutableListOf()) : AssertAction {

    fun text(textConfigure: () -> String): TextAssertion {
        return TextAssertion(textConfigure())
    }

    infix fun TextAssertion.display(expect: Boolean) {
        this.expect = expect
        assertions += this
    }

    data class TextAssertion(var text: String, var expect: Boolean? = null) : Assertion

}