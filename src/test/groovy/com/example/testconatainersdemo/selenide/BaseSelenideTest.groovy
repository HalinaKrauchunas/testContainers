package com.example.testconatainersdemo.selenide

import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.*
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

import static com.codeborne.selenide.Configuration.baseUrl
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB

@SpringBootTest
@Testcontainers
@ActiveProfiles('test')
abstract class BaseSelenideTest extends Specification {

    @Value('${selenide.user.email}')
    def accEmail
    @Value('${selenide.user.password}')
    def accPassword
    @Value('${selenide.user.userId}')
    def userId
    @Value('${selenide.user.deviceId}')
    def deviceId
    @Value('${selenide.user.pin}')
    def pin

    static Network network = Network.newNetwork()

    static GenericContainer demoContainer = new GenericContainer(
            DockerImageName.parse("demotestcontainers:latest"))
            .withNetwork(network)
            .withExposedPorts(8080)
            .withNetworkAliases('demo')
            .withReuse(true)
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("demo")))


    static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions())
            .withNetwork(network)
            .withNetworkAliases("chrome")
            .withReuse(true)
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("/home/kravchunas/Downloads/testconatainersDemo/src/test/recording"), VncRecordingContainer.VncRecordingFormat.MP4)

    static {
        demoContainer.start()
        chrome.start()
        baseUrl = ''
    }
}