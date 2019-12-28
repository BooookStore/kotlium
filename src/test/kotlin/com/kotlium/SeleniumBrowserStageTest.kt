package com.kotlium

import com.kotlium.selenium.SeleniumWebDriverWrapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.xpath
import org.openqa.selenium.NoSuchSessionException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class SeleniumBrowserStageTest {

    private lateinit var webDriverWrapper: IWebDriverWrapper

    private lateinit var driver: WebDriver

    @BeforeEach
    fun beforeEach() {
        driver = RemoteWebDriver(URL("http://localhost:4444/wd/hub"), ChromeOptions())
        webDriverWrapper = SeleniumWebDriverWrapper(driver)
    }

    @AfterEach
    fun afterEach() {
        try {
            driver.close()
            throw Exception("セッションが破棄されていません")
        } catch (e: NoSuchSessionException) {
            // テスト内でセッションを破棄できなかった際のリトライのため、例外を無視
        }
    }

    @Test
    fun githubTest() {
        val config = BrowserStageConfiguration("github", "https://github.co.jp/")

        val stageExecuteResult = BrowserStage(config, webDriverWrapper) {
            click { target = xpath("//a[normalize-space() = '機能']") }
            waitForUrl("https://github.co.jp/features")
            assertPage {
                assertThat(findElement(xpath("//*[normalize-space() = '効率的な開発ワークフロー']")).isDisplayed).isTrue()
            }
            click { target = xpath("//a[normalize-space() = 'GitHub Marketplaceにアクセスする']") }
            waitForUrl("https://github.com/marketplace")
            input {
                target = xpath("//input[@name='query']")
                value = "circle ci"
                inputEnter()
            }
        }.execute()

        assertThat(stageExecuteResult.isOk).isTrue()
    }

    @Test
    fun githubFailedTest() {
        val config = BrowserStageConfiguration("github", "https://github.co.jp/")

        // execute
        val stageExecutedResult = BrowserStage(config, webDriverWrapper) {
            click { target = xpath("//a[normalize-space() = '機能']") }
            waitForUrl("https://github.co.jp/features")
            assertPage {
                assertThat(findElement(xpath("//*[normalize-space() = 'not exist element']")).isDisplayed).isTrue()
            }
            click { target = xpath("//a[normalize-space() = 'GitHub Marketplaceにアクセスする']") }
        }.execute()

        // verify
        assertThatThrownBy { driver.close() }.isExactlyInstanceOf(NoSuchSessionException::class.java)
        assertThat(stageExecutedResult.isOk).isFalse()
        assertThat(stageExecutedResult.executedActions).hasSize(3)
    }

}