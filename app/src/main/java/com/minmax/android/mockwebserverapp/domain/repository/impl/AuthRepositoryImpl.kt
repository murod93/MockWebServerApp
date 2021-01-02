package com.minmax.android.mockwebserverapp.domain.repository.impl

import com.minmax.android.mockwebserverapp.data.source.remote.api.AuthApi
import com.minmax.android.mockwebserverapp.data.source.remote.model.*
import com.minmax.android.mockwebserverapp.domain.repository.AuthRepository
import com.minmax.android.mockwebserverapp.util.ext.flowSafe
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by murodjon on 2020/12/24
 */
class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : AuthRepository {

    override fun registration(registrationRequest: RegistrationRequest): Flow<NetworkData<Any>> =
        flowSafe {
            val response = authApi.registration(registrationRequest)

            when {
                response.body() == null -> {
                    emit(NetworkData.message("Error occurred!"))
                }
                response.body() != null -> {
                    if (response.code() == 201) {
                        emit(NetworkData.data(Any()))
                    } else {
                        emit(NetworkData.message(response.body()?.msgCode!!))
                    }
                }
            }
        }

    override fun login(loginRequest: LoginRequest): Flow<NetworkData<LoginResponse.LoginData>> =
        flowSafe {
            val response = authApi.login(loginRequest)

            when {
                response.body() == null -> {
                    emit(NetworkData.message("Error occurred!"))
                }
                response.body() != null && response.body()?.data != null -> {
                    emit(NetworkData.data(response.body()?.data!!))
                }
                response.body() != null -> {
                    emit(NetworkData.message(response.message()))
                }
            }
        }

    override suspend fun loginNoFlow(loginRequest: LoginRequest): Response<Wrapper<LoginResponse.LoginData>> =
        authApi.login(loginRequest)
}