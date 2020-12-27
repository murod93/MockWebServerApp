package com.minmax.android.mockwebserverapp.data.source.remote.api

import com.minmax.android.mockwebserverapp.data.source.remote.model.LoginRequest
import com.minmax.android.mockwebserverapp.data.source.remote.model.LoginResponse
import com.minmax.android.mockwebserverapp.data.source.remote.model.RegistrationRequest
import com.minmax.android.mockwebserverapp.data.source.remote.model.Wrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by murodjon on 2020/12/23
 *
 * Let's use http://dummy.restapiexample.com/# for testing purposes
 */
interface AuthApi {

    @POST("/v1/login")
    suspend fun login(@Body request: LoginRequest):Response<Wrapper<LoginResponse.LoginData>>

    @POST("/v1/registration")
    suspend fun registration(@Body request: RegistrationRequest):Response<Wrapper<Any>>
}