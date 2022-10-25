package com.example.testconatainersdemo.dynamodb2redis

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.*
import com.example.testconatainersdemo.entity.Dev
import com.example.testconatainersdemo.service.ResponseServiceTest
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import spock.lang.Shared

import static com.amazonaws.services.dynamodbv2.model.KeyType.HASH
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB

class DynamoDb2RedisControllerTest extends BaseEnd2EndTest {

    public static final String DYNAMO_DB_CONTROLLER_PATH = ResponseServiceTest.HOST_DEMO_CONTAINER + '/dynamo/'

    @Shared
    AmazonDynamoDB dynamoDbClient

    @Shared
    DynamoDB dynamoDB

    @Shared
    RedissonClient redissonClient


    def setupSpec() {
        def config = new Config()
        config.useSingleServer().setAddress("redis://" + redis.getHost() + ":" + redis.getMappedPort(6379))
        redissonClient = Redisson.create(config)

        dynamoDbClient = AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(localstack.getEndpointConfiguration(DYNAMODB))
            .withCredentials(localstack.getDefaultCredentialsProvider())
            .build()

        dynamoDB = new DynamoDB(dynamoDbClient)
    }

    def "should return correct response from dynamodb and add HALINA"() {
        given:
            def table = 'Dev'
            def attribute = 'devId'
        and:
            createTable(table, attribute, 'S')
        and:
            def attributeValues = new HashMap<String, AttributeValue>()
            attributeValues.put(attribute, new AttributeValue().withS(value))
        and:
            putItem(table, attributeValues)

        when:
            def response = ResponseServiceTest.getRequest(DYNAMO_DB_CONTROLLER_PATH + "$value")

        then:
            response == """{"devId":"${value}_HALINA"}"""

        where:
            value << ['devId1', 'devId2', 'otherDevId']
    }

    def "should return correct response from redis"() {
        given:
            def rMap = redissonClient.getMap('MyMap') as Map<String, Dev>
            def value = 'value'
            def expectedDevId = 'valueDeviceId'
            rMap.put(value, new Dev(devId: expectedDevId))
        when:
            def response = ResponseServiceTest.getRequest(DYNAMO_DB_CONTROLLER_PATH + value)

        then:
            response == """{"devId":"${expectedDevId}"}"""
    }

    def createTable(String tableName, String attribute, String typeAttr) {
        def attributeDefinitions = new ArrayList<AttributeDefinition>()
        attributeDefinitions.add(createAttribute(attribute, typeAttr))


        def keySchema = new ArrayList<KeySchemaElement>()
        keySchema.add(new KeySchemaElement()
            .withAttributeName(attribute)
            .withKeyType(HASH))

        def request = new CreateTableRequest()
            .withTableName(tableName)
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L))

        def table = dynamoDB.createTable(request)
        table.waitForActive()
    }

    def createAttribute(name, type) {
        new AttributeDefinition()
            .withAttributeName(name)
            .withAttributeType(type)
    }

    def putItem(String tableName, attributeValues) {
        PutItemRequest putItemRequest = new PutItemRequest()
            .withTableName(tableName)
            .withItem(attributeValues)
        dynamoDbClient.putItem(putItemRequest)
    }
}