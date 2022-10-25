package com.example.testconatainersdemo.dynamodb2redis


import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.Network
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
abstract class BaseEnd2EndTest extends Specification {

    static Network network = Network.newNetwork()

    static LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.3"))
            .withServices(DYNAMODB)
            .withNetwork(network)
            .withReuse(true)
            .withNetworkAliases('dynamodb')

    static GenericContainer redis = new GenericContainer<>("redis:5.0.3-alpine")
            .withNetwork(network)
            .withNetworkAliases('redis')
            .withExposedPorts(6379)

    static GenericContainer demoContainer = new GenericContainer(
            DockerImageName.parse("demotestcontainers:latest"))
            .withNetwork(network)
            .withExposedPorts(8080)
            .withNetworkAliases('demo')
            .withEnv("aws.dynamoDb.endpoint", "http://dynamodb:4569")
            .withEnv("AWS_ACCESS_KEY", "access")
            .withEnv("AWS_SECRET_KEY", "secret")
            .withEnv("redis.host", "redis")
            .withEnv("redis.port", "6379")
            .withEnv("mock.url", "http://mock:1080")
            .withReuse(true)
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("demo")))


    static MockServerContainer mockContainer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:mockserver-5.9.0"))
            .withNetwork(network)
            .withExposedPorts(1080)
            .withReuse(true)
            .withNetworkAliases('mock')

    static {
        redis.start()
        localstack.start()
        mockContainer.start()
        demoContainer.start()
    }
}