package com.kotlium.integration

import com.kotlium.Scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.xpath
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions.urlToBe
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class IntegrationTest {

    @Rule
    private val browserWebDriverContainer = BrowserWebDriverContainer<Nothing>()
        .apply {
            withCapabilities(ChromeOptions())
            start()
        }

    @Test
    fun githubTest() {
        // setup and execute
        val scenarioExecuteResult = Scenario {
            browserStage("https://github.co.jp/") {
                click {
                    xpath("//a[normalize-space() = '機能']")
                }
                waitFor {
                    urlToBe("https://github.co.jp/features")
                }
                assertPage {
                    assertThat(findElement(xpath("//*[normalize-space() = '効率的な開発ワークフロー']")).isDisplayed).isTrue()
                }
                click {
                    xpath("//a[normalize-space() = 'GitHub Marketplaceにアクセスする']")
                }
                waitFor {
                    urlToBe("https://github.com/marketplace")
                }
                input {
                    target = xpath("//input[@name='query']")
                    value = "circle ci"
                    lastEnter = true
                }
            }
        }.execute(browserWebDriverContainer.webDriver)

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
    }

}