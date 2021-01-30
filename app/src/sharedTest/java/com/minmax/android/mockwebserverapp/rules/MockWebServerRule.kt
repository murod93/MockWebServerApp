package com.minmax.android.mockwebserverapp.rules
/*
 * Copyright (C) 2020 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Runs MockWebServer for the duration of a single test method.
 *
 * In Java JUnit 4 tests (ie. tests annotated `@org.junit.Test`), use this by defining a field with
 * the `@Rule` annotation:
 *
 * ```
 * @Rule public final MockWebServerRule serverRule = new MockWebServerRule();
 * ```
 *
 * For Kotlin the `@JvmField` annotation is also necessary:
 *
 * ```
 * @JvmField @Rule val serverRule = MockWebServerRule()
 * ```
 */
class MockWebServerRule : ExternalResource() {
    val server: MockWebServer = MockWebServer()

    override fun before() {
        try {
            server.start(8080)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun after() {
        try {
            server.shutdown()
        } catch (e: IOException) {
            logger.log(Level.WARNING, "MockWebServer shutdown failed", e)
        }
    }

    companion object {
        private val logger = Logger.getLogger(MockWebServerRule::class.java.name)
    }
}