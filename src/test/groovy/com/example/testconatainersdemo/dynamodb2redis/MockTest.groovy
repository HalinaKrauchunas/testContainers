package com.example.testconatainersdemo.dynamodb2redis

import com.example.testconatainersdemo.service.MockServiceTest
import com.example.testconatainersdemo.service.ResponseServiceTest

class MockTest extends BaseEnd2EndTest {

    public static final String MOCK_CONTROLLER_PATH = ResponseServiceTest.HOST_DEMO_CONTAINER + '/mock'

    def setup() {

        String returnJson = """{
                "deviceId" : "deviceId",
                "userId" : "userId"
                }"""

        MockServiceTest.createMock('GET', "/hello", returnJson)

    }

    def "check mock response"() {
        when:
            def response = ResponseServiceTest.getRequest(MOCK_CONTROLLER_PATH)

        then:
            response == """{
                "deviceId" : "deviceId",
                "userId" : "userId"
                }"""
    }
}
