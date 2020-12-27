package com.minmax.android.mockwebserverapp.domain.repository

import com.minmax.android.mockwebserverapp.data.source.remote.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * Created by murodjon on 2020/12/24
 */
interface AuthRepository {
    fun registration(registrationRequest: RegistrationRequest): Flow<NetworkData<Any>>
    fun login(loginRequest: LoginRequest): Flow<NetworkData<LoginResponse.LoginData>>
    suspend fun loginNoFlow(loginRequest: LoginRequest): Response<Wrapper<LoginResponse.LoginData>>
}