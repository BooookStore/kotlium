package com.kotlium

import com.kotlium.selenium.SeleniumWebDriverWrapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.xpath
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.urlToBe
import org.openqa.selenium.support.ui.WebDriverWait
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
        } catch (e: Exception) {
            // テスト内でセッションを破棄できなかった際のリトライのため、例外を無視
        }
    }

    @Test
    fun githubTest() {
        val config = BrowserStageConfiguration("github", "https://github.co.jp/")

        val stageExecuteResult = BrowserStage(config, webDriverWrapper) {
            click { target = xpath("//a[normalize-space() = '機能']") }
            assertPage {
                WebDriverWait(this, 5).until(urlToBe("https://github.co.jp/features"))
                assertThat(findElement(xpath("//*[normalize-space() = '効率的な開発ワークフロー']")).isDisplayed).isTrue()
            }
            click { target = xpath("//a[normalize-space() = 'GitHub Marketplaceにアクセスする']") }
            assertPage {
                WebDriverWait(this, 5).until(urlToBe("https://github.com/marketplace"))
            }
            input {
                target = xpath("""//input[@name='query']""")
                value = "circle ci"
                inputEnter()
            }
        }.execute()

        stageExecuteResult.executedActions.forEach { println(it.message.firstOrNull() ?: "") }
        assertThat(stageExecuteResult.isOk).isTrue()
    }

}