package com.minmax.android.mockwebserverapp.util

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Created by murodjon on 2020/12/27
 */
fun MockWebServer.setUpMockServerResponse(
    responseCode: Int,
    responseMessage: String,
    responseBody: String
) {
    val mockResponse = MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .setBody(responseBody)
        .setStatus(String.format("HTTP/1.1 %s %s", responseCode, responseMessage))
//        .throttleBody(1024, 1, TimeUnit.SECONDS)// simulating network conditions
    this.enqueue(mockResponse)
}

fun MockWebServer.setUpMockServerResponse(
    responseCode: Int,
    responseBody: String
) {
    val mockResponse = MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .setBody(responseBody)
        .setResponseCode(responseCode)
//        .throttleBody(1024, 1, TimeUnit.SECONDS)// simulating network conditions
    this.enqueue(mockResponse)
}