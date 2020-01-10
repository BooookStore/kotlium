package com.kotlium.integration

import com.kotlium.Scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.jupiter.api.Test
import org.openqa.selenium.By.xpath
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.net.URL
import java.time.Duration

@Testcontainers
class IntegrationTest {

    @ClassRule
    val dockerComposeContainer =
        DockerComposeContainer<Nothing>(File("src/test/resources/com/kotlium/integration/docker-compose.yml")).apply {
            withExposedService("web_1", 8080, Wait.forHttp("/book/list"))
            withLogConsumer("web_1", Slf4jLogConsumer(LoggerFactory.getLogger("web_1")))
            withExposedService("webDriver_1", 4444, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
            withLogConsumer("webDriver_1", Slf4jLogConsumer(LoggerFactory.getLogger("webDriver_1")))
            start()
        }

    @Test
    fun integrationTest() {
        val seleniumServerHost = dockerComposeContainer.getServiceHost("webDriver_1", 4444)
        val seleniumServerPort = dockerComposeContainer.getServicePort("webDriver_1", 4444)

        // setup and execute
        val scenarioExecuteResult = Scenario {
            browserStage("web:8080/book/list") {
                assertPage {
                    assertThat(findElement(xpath("//body/p")).text).isEqualTo("ようこそ")
                }
            }
        }.execute(RemoteWebDriver(URL("http://$seleniumServerHost:$seleniumServerPort/wd/hub"), ChromeOptions()))

        // verify
        assertThat(scenarioExecuteResult.isOk).isTrue()
    }

}