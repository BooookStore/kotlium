package com.kotlium

import com.kotlium.selenium.SeleniumWebDriverWrapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class SeleniumBrowserStageTest {

    lateinit var webDriverWrapper: IWebDriverWrapper

    lateinit var driver: WebDriver

    @BeforeEach
    fun beforeEach() {
        driver = RemoteWebDriver(URL("http://localhost:4444/wd/hub"), ChromeOptions())
        webDriverWrapper = SeleniumWebDriverWrapper(driver)
    }

    @Test
    fun githubTest() {
        val config = BrowserStageConfiguration("github", "https://github.co.jp/")

        val stageExecuteResult = BrowserStage(config, webDriverWrapper) {
            click { target = XPath.link("機能") }
            assertPage {
                url equals "https://github.co.jp/features"
                text { "効率的な開発ワークフロー" } display true
            }
            click { target = XPath.link("GitHub Marketplaceにアクセスする") }
            assertPage {
                url equals "https://github.com/marketplace"
            }
            input {
                target = XPath("""//input[@name='query']""")
                value = "circle ci"
                inputEnter()
            }
        }.execute()

        stageExecuteResult.executedActions.forEach { println(it.message.firstOrNull() ?: "") }
        assertThat(stageExecuteResult.isOk).isTrue()
    }

}