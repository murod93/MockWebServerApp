package com.minmax.android.mockwebserverapp.util.ext

import com.minmax.android.mockwebserverapp.data.source.remote.model.NetworkData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.lang.Exception

/**
 * Created by murodjon on 2020/12/24
 */
fun <T> flowSafe(block:suspend FlowCollector<NetworkData<T>>.()->Unit):Flow<NetworkData<T>> = flow {
    try {
        block()
    }catch (e:Exception){
        NetworkData.failure<T>(e)
    }
}