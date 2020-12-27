package com.minmax.android.mockwebserverapp.util

import java.io.InputStreamReader

/**
 * Created by murodjon on 2020/12/24
 */
class MockResponseFileReader(path: String) {
    val content: String
    init {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}