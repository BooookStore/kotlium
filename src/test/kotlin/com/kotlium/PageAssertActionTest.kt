package com.kotlium

import com.kotlium.PageAssertAction.TextAssertion
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PageAssertActionTest {

    @Test
    fun pageAssertActionTest() {
        // setup
        val mockIWebDriverWrapper = mockk<IWebDriverWrapper>()
        every { mockIWebDriverWrapper.isTextDisplay(any()) } returns true
        every { mockIWebDriverWrapper.isTextDisplay("!") } returns false

        // execute
        val executeResult = PageAssertAction(
            TextAssertion("hello", true),
            TextAssertion("world", true),
            TextAssertion("!", false)
        ).execute(mockIWebDriverWrapper)

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.actionClass).isEqualTo(PageAssertAction::class)
        assertThat(executeResult.message).isEqualTo("'!' is not display")
    }

}