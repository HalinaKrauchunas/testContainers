package com.example.testconatainersdemo.service

import com.example.testconatainersdemo.dynamodb2redis.BaseEnd2EndTest

import java.net.http.HttpClient
import java.net.http.HttpResponse

class ResponseServiceTest {

    private static final String HOST_GOLDEN_DEV = 'http://hmsgoogleapi-goldendev.arlocloud.com'
    private static final String DISARM_URL = HOST_GOLDEN_DEV + '/hmsgoogleapi/v1/devices/disarm'
    private static final String GET_STATE_URL = HOST_GOLDEN_DEV + '/hmsgoogleapi/v1/devices/states'
    public static final String HOST_DEMO_CONTAINER = 'http://' + BaseEnd2EndTest.demoContainer.getHost() + ':' + BaseEnd2EndTest.demoContainer.getMappedPort(8080)


    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()

    static String getBodyAfterDisarmDevice(String userId, String deviceId, String pin) {

        def jsonString =
            """{
                "ids": ["${deviceId}"],
                "pin": "${pin}"
            }"""

        def postDisarm = RequestServiceTest.postOfString(DISARM_URL, userId, jsonString)

        HTTP_CLIENT.send(postDisarm, HttpResponse.BodyHandlers.ofString()).body()
    }

    static String getBodyAfterGetStateForDevice(String userId, String deviceId) {

        def jsonString =
            """{
                "ids": "${deviceId}"
                }
            """

        def postDisarm = RequestServiceTest.postOfString(GET_STATE_URL, userId, jsonString)

        HTTP_CLIENT.send(postDisarm, HttpResponse.BodyHandlers.ofString()).body()
    }

    static String getRequest(String path) {

        def getTest = RequestServiceTest.getOfString(path)

        HTTP_CLIENT.send(getTest, HttpResponse.BodyHandlers.ofString()).body()
    }
}
