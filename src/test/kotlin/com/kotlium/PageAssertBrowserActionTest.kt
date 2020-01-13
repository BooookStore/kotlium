package com.kotlium

import com.kotlium.action.PageAssertBrowserAction
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver

internal class PageAssertBrowserActionTest {

    @Test
    fun pageAssertActionTest() {
        // setup
        val driver: WebDriver = mockk(relaxed = true)

        // execute
        val executeResult = PageAssertBrowserAction {
            throw Exception("this is failed message")
        }.execute(driver)

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.browserActionClass).isEqualTo(PageAssertBrowserAction::class)
        assertThat(executeResult.message).containsExactlyInAnyOrder("assert failed", "this is failed message")
    }

}