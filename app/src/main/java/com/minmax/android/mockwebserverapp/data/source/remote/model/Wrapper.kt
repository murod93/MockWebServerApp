package com.minmax.android.mockwebserverapp.data.source.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by murodjon on 2020/12/24
 */
data class Wrapper<T>(
        @field:SerializedName("data")
        val data:T? = null,

        @field:SerializedName("msgCode")
        val msgCode:String?=null
)