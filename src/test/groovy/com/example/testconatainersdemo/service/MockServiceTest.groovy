package com.example.testconatainersdemo.service

import com.example.testconatainersdemo.dynamodb2redis.BaseEnd2EndTest
import org.mockserver.client.MockServerClient
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class MockServiceTest {

    static void createMock(String method, String path, String jsonBody){

        new MockServerClient(BaseEnd2EndTest.mockContainer.host, BaseEnd2EndTest.mockContainer.serverPort)
            .when(
                request()
                    .withMethod(method)
                    .withPath(path))
            .respond(response()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonBody))
    }
}
