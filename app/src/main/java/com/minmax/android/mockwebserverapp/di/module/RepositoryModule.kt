package com.minmax.android.mockwebserverapp.di.module

import com.minmax.android.mockwebserverapp.domain.repository.AuthRepository
import com.minmax.android.mockwebserverapp.domain.repository.impl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by murodjon on 2021/01/02
 */
@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}