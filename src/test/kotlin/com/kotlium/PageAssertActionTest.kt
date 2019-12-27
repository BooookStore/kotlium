package com.kotlium

import com.kotlium.action.PageAssertAction
import com.kotlium.action.TextAssertion
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
            TextAssertion("!", true)
        ).execute(mockIWebDriverWrapper)

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.actionClass).isEqualTo(PageAssertAction::class)
        assertThat(executeResult.message).containsExactlyInAnyOrder("'!' is not display")
    }

}