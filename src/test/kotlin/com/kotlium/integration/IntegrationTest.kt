package com.kotlium.integration

import com.kotlium.Scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.xpath
import org.openqa.selenium.chrome.ChromeOptions
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class IntegrationTest {

    lateinit var browserWebDriverContainer: BrowserWebDriverContainer<Nothing>

    @BeforeEach
    fun beforeEach() {
        val network = Network.newNetwork()

        browserWebDriverContainer = BrowserWebDriverContainer<Nothing>().apply {
            withNetwork(network)
            withCapabilities(ChromeOptions())
        }

        val testWebbApp = GenericContainer<Nothing>("bookstore/kotlium-test-webapp:0.0.1").apply {
            withNetwork(network)
            withExposedPorts(8080)
            waitingFor(Wait.forHttp("/book/list"))
            withNetworkAliases("testWebApp")
        }

        testWebbApp.start()
        browserWebDriverContainer.start()
    }

    @Test
    fun githubTest() {
        // setup and execute
        val scenarioExecuteResult = Scenario {
            browserStage("testWebApp:8080/book/list") {
                assertPage {
                    assertThat(findElement(xpath("//body/p")).text).isEqualTo("ようこそ")
                }
            }
        }.execute(browserWebDriverContainer.webDriver)

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
    }

}