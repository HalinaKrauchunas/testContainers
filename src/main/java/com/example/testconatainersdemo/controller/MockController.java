package com.example.testconatainersdemo.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping
@RequiredArgsConstructor
@AllArgsConstructor
public class MockController {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    @Value("${mock.url}")
    String url;

    @GetMapping(value = "/mock")
    public String test() throws IOException, InterruptedException {

       return HTTP_CLIENT.send(HttpRequest.newBuilder()
                    .uri(URI.create(url + "/hello"))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString())
            .body();
    }
}
