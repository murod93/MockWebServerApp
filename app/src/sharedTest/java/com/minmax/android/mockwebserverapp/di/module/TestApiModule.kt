package com.minmax.android.mockwebserverapp.di.module

import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.junit.Assert.*
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by murodjon on 2021/01/04
 */
@InstallIn(ApplicationComponent::class)
@Module
class TestApiModule{
    @Singleton
    @Provides
    @Named("test_auth_api")
    fun provideAuthApi(@Named("test_retrofit")retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}