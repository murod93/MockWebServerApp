package com.minmax.android.mockwebserverapp.data.source.remote.model

/**
 * Created by murodjon on 2020/12/24
 */
sealed class NetworkData<T> {
    internal class Failure<T> internal constructor(val exception: Throwable):NetworkData<T>()
    internal class Data<T> internal constructor(val data:T):NetworkData<T>()
    internal class Message<T> internal constructor(val msg:String):NetworkData<T>()

    fun isMessage() = this is Message
    fun isData() = this is Data<T>
    fun isFailure() = this is Failure

    fun getMessageOrNull() = (this as? Message<T>)?.msg
    fun getFailureOrNull() = (this as? Failure<T>)?.exception
    fun getDataOrNull() = (this as? Data<T>)?.data

    inline fun onData(f:(T)->Unit):NetworkData<T>{
        if (isData()) getDataOrNull()?.let { f(it) }
        return this
    }

    inline fun onFailure(f:(Throwable)->Unit):NetworkData<T>{
        if (isFailure())getFailureOrNull()?.let { f(it) }
        return this
    }

    inline fun onMessage(f:(String)->Unit):NetworkData<T>{
        if (isMessage())getMessageOrNull()?.let { f(it) }
        return this
    }

    companion object{
        fun <T> data(data:T):NetworkData<T> = Data(data)
        fun <T> failure(e:Throwable):NetworkData<T> = Failure(e)
        fun <T> message(msg:String):NetworkData<T> = Message(msg)
    }
}