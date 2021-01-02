package com.minmax.android.mockwebserverapp.di.module

import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by murodjon on 2021/01/02
 */
@InstallIn(ApplicationComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

}