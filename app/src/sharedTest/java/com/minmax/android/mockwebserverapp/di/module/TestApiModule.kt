package com.minmax.android.mockwebserverapp.di.module

import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by murodjon on 2021/01/04
 */
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
@Module
class TestApiModule{
    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}