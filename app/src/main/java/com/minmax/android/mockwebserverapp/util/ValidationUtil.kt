package com.minmax.android.mockwebserverapp.util

import android.util.Patterns

/**
 * Created by murodjon on 2021/01/02
 */
object ValidationUtil {
    fun isValidEmail(email:String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
//        return email.matches(emailPattern.toRegex())
    }
}