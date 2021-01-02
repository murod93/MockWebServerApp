package com.minmax.android.mockwebserverapp.domain.repository.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import com.minmax.android.mockwebserverapp.data.source.remote.model.LoginRequest
import com.minmax.android.mockwebserverapp.data.source.remote.model.RegistrationRequest
import com.minmax.android.mockwebserverapp.domain.repository.AuthRepository
import com.minmax.android.mockwebserverapp.rules.MockWebServerRule
import com.minmax.android.mockwebserverapp.util.MockResponseFileReader
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by murodjon on 2020/12/24
 * very good example @link https://stackoverflow.com/a/35826483/9622186
 * https://www.raywenderlich.com/10091980-testing-rest-apis-using-mockwebserver
 * Official github: https://github.com/square/okhttp/tree/master/mockwebserver
 * https://speakerdeck.com/heyitsmohit/unit-testing-kotlin-channels-and-flows-android-summit?slide=47
 */
@RunWith(JUnit4::class)
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository

    @get:Rule
    val serverRule = MockWebServerRule()

    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

//    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        serverRule.server.dispatcher = dispatcher
        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        val authApi = Retrofit.Builder()
            .baseUrl(serverRule.server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)

        authRepository = AuthRepositoryImpl(authApi)
    }

    private val dispatcher: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            when (request.path) {
                "/v1/login" -> return MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody(MockResponseFileReader("login/sample_response.json").content)

                "/v1/registration" -> return MockResponse()
                    .setResponseCode(201)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody(MockResponseFileReader("registration/sample_response.json").content)
            }
            return MockResponse().setResponseCode(404)
        }
    }

    @Test
    fun `read sample response json file return not null `() {
        val content = MockResponseFileReader("login/sample_response.json").content
        assertNotNull(content)
    }

    @Test
    fun `given valid user name and password when login returns true`() = runBlocking {
//        serverRule.server.apply {
//            enqueue(
//                MockResponse()
//                    .setResponseCode(200)
//                    .addHeader("Content-Type", "application/json; charset=utf-8")
//                    .addHeader("Cache-Control", "no-cache")
//                    .setBody(MockResponseFileReader("login/sample_response.json").content)
//            )
//        }

        val result =
            authRepository.login(LoginRequest("example@techtalk.com", "example123#")).first()
        print(result.getDataOrNull())
        assertNotNull(result.getDataOrNull())
        assertEquals("example@techtalk.com", result.getDataOrNull()?.user?.email)
    }

    @Test
    fun `given valid user name and password when login without flow returns true`() = runBlocking {
//        serverRule.server.apply {
//            enqueue(
//                MockResponse()
//                    .setResponseCode(200)
//                    .addHeader("Content-Type", "application/json; charset=utf-8")
//                    .addHeader("Cache-Control", "no-cache")
//                    .setBody(MockResponseFileReader("login/sample_response.json").content)
//            )
//        }

        val result = authRepository.loginNoFlow(LoginRequest("example@techtalk.com", "example123#"))
        print(result.body()?.data?.user)

        assertNotNull(result.body())
        assertEquals(200, result.code())

        val record = serverRule.server.takeRequest()
        print(record.getHeader("Content-Type"))
    }

    @Test
    fun `given valid user information when registration returns true`() = runBlocking {
//        serverRule.server.apply {
//            enqueue(
//                MockResponse()
//                    .setResponseCode(201)
//                    .addHeader("Content-Type", "application/json; charset=utf-8")
//                    .addHeader("Cache-Control", "no-cache")
//                    .setBody(MockResponseFileReader("registration/sample_response.json").content)
//            )
//        }

        val result = authRepository.registration(
            RegistrationRequest(
                "example@techtalk.com",
                "example123#",
                "example123#",
                "tech talk"
            )
        ).first()
        print(result.getDataOrNull())

        val record = serverRule.server.takeRequest()
        print(record.getHeader("Content-Type"))
    }

    @Test
    fun `test coroutine`() = runBlocking {
        val m = flow {
            emit(1)
            emit(2)
        }
        m.collect {
            print(it)
        }
        assertEquals(
            listOf(1, 2),
            m.toList()
        )
    }

    @After
    fun tearDown() {

    }
}