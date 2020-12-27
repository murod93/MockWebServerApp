package com.minmax.android.mockwebserverapp.data.source.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by murodjon on 2020/12/24
 */
sealed class LoginResponse {
    data class LoginData(
        @field:SerializedName("user")
        val user:User? = null,
        @field:SerializedName("token")
        val token: Token? = null
    )
    data class User(
        var userName:String="",
        var email:String="",
        var phoneNumber:String="",
        var firstName:String="",
        var lastName:String="")
    data class Token(
        var accessToken:String="",
        var refreshToken:String=""
    )
}