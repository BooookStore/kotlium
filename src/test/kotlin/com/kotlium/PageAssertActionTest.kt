package com.kotlium

import com.kotlium.action.PageAssertAction
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver

internal class PageAssertActionTest {

    @Test
    fun pageAssertActionTest() {
        // setup
        val driver: WebDriver = mockk(relaxed = true)

        // execute
        val executeResult = PageAssertAction {
            throw Exception("this is failed message")
        }.execute(driver)

        // verify
        assertThat(executeResult.isOk).isFalse()
        assertThat(executeResult.actionClass).isEqualTo(PageAssertAction::class)
        assertThat(executeResult.message).containsExactlyInAnyOrder("this is failed message")
    }

}