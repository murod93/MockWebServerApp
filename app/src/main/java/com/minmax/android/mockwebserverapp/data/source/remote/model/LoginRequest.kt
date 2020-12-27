package com.minmax.android.mockwebserverapp.data.source.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by murodjon on 2020/12/24
 */
data class LoginRequest(
    @field:SerializedName("email")var email:String="",
    @field:SerializedName("password")var password:String=""
)