package com.minmax.android.mockwebserverapp.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.minmax.android.mockwebserverapp.BuildConfig
import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import com.minmax.android.mockwebserverapp.data.source.remote.model.LoginResponse
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by murodjon on 2020/12/27
 */
class NetworkModule {

    fun provideLoggingInterceptor(): HttpLoggingInterceptor =  HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun provideGson(): Gson = GsonBuilder()
        .apply {
            setPrettyPrinting()
//            setExclusionStrategies(HiddenAnnotationExclusionStrategy())
        }.create()


    fun provideRetrofitAuth(baseUrl:String, client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun provideRetrofit(baseUrl:String, client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun providePublicClient(context: Context, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG){
            builder
//                .addInterceptor(ChuckInterceptor(context))/** Comment before pushing to live*/
                .addInterceptor(loggingInterceptor)/** Comment before pushing to live*/

        }
        return builder.build()
    }

    fun provideClientPrivate(context: Context, loggingInterceptor: HttpLoggingInterceptor, authenticator: Authenticator): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor {
                val old = it.request()
                val request = old.newBuilder()
                    .removeHeader("Authorization")
//                    .addHeader("Authorization", "${db.token().accessToken}")
                    .method(old.method, old.body)
                    .build()
                it.proceed(request)
            }
            .authenticator(authenticator)
        if (BuildConfig.DEBUG){
            builder
//                .addInterceptor(ChuckInterceptor(context))/** Comment before pushing to live*/
                .addInterceptor(loggingInterceptor)/** Comment before pushing to live*/

        }
        return builder.build()

    }

    fun provideAuthenticator(retrofit: Retrofit/*, db: DB*/): Authenticator = object :
        Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
//            if (db.token().isEmpty()) {
//                EventBus.logoutLiveData.postValue(Unit)
//                return null
//            }
//            if (response.code == 401) {
//                val refreshToken = retrofit.create(AuthApi::class.java).refreshToken(db.token()).execute()
//                if (refreshToken.code() == 401) {
//                    db.token(LoginResponse.Token())
////                    EventBus.logoutLiveData.postValue(Unit)
//                    return null
//                }
//                db.token(refreshToken.body()?.data ?: LoginResponse.Token())
//            }
            return response.request.newBuilder()
                .removeHeader("Authorization")
                //.addHeader("Authorization", "${db.token().accessToken}")
                .removeHeader("user-agent")
                .addHeader("user-agent", "Android")
                .build()
        }
    }
}