package com.minmax.android.mockwebserverapp.util

/**
 * Created by murodjon on 2021/01/02
 */
object ValidationUtil {
    fun isValidEmail(email:String):Boolean{
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}