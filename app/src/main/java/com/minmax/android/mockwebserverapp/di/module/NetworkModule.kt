package com.minmax.android.mockwebserverapp.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.minmax.android.mockwebserverapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by murodjon on 2020/12/27
 */
@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .apply {
            setPrettyPrinting()
//            setExclusionStrategies(HiddenAnnotationExclusionStrategy())
        }.create()

    @Provides
    fun provideBaseUrl(): String = "https://api.com/"

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun provideClientPrivate(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor {
                val old = it.request()
                val request = old.newBuilder()
                    .removeHeader("Authorization")
//                    .addHeader("Authorization", "${db.token().accessToken}")
                    .method(old.method, old.body)
                    .build()
                it.proceed(request)
            }
        if (BuildConfig.DEBUG) {
            builder
//                .addInterceptor(ChuckInterceptor(context))/** Comment before pushing to live*/
                .addInterceptor(loggingInterceptor)
            /** Comment before pushing to live*/

        }
        return builder.build()

    }
}