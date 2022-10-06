package com.example.testconatainersdemo.service

import java.net.http.HttpRequest

class RequestServiceTest {

     static HttpRequest postOfString(String url, String userId, String jsonInputString) {

        HttpRequest.newBuilder()
             .uri(URI.create(url))
             .header("Content-Type", "application/json")
             .header("OAuth-User-Id", "${userId}")
             .header("OAuth-App-Id", "google")
             .header("OAuth-Scope", "Arlo")
             .header("X-Request-ID", "test-req-id")
             .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
             .build()
    }

    static HttpRequest getOfString(String url) {

        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build()
    }
}