package com.minmax.android.mockwebserverapp.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.minmax.android.mockwebserverapp.BuildConfig
import com.minmax.android.mockwebserverapp.rules.MockWebServerRule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by murodjon on 2021/01/04
 */

@Module
@InstallIn(ApplicationComponent::class)
class TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockServer (): MockWebServer {
        var mockWebServer: MockWebServer? = null
        val thread = Thread {
            mockWebServer = MockWebServer()
            mockWebServer?.start()
        }
        thread.start()
        thread.join()
        return mockWebServer ?: throw NullPointerException()
    }

    @Provides
    @Singleton
    @Named("test_base_url")
    fun provideBaseUrl (mockWebServer:MockWebServer): String {
        var url = ""
        val t = Thread {
            url = mockWebServer.url("/").toString()
        }
        t.start()
        t.join()
        return url
    }

//    @Provides
//    @Named("test_base_url")
//    fun provideBaseUrl(): String = "http://localhost:8080/"

    @Singleton
    @Provides
    @Named("test_retrofit")
    fun provideRetrofit(@Named("test_base_url")baseUrl: String, client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}