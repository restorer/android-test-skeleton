package com.example.test.feature.logging

import android.util.Log
import com.example.test.feature.di.IsLogTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor(@IsLogTag private val logTag: String) {
    fun info(message: String) {
        Log.i(logTag, message)
    }

    fun error(message: String) {
        Log.e(logTag, message)
    }

    fun error(message: String, t: Throwable) {
        Log.e(logTag, message, t)
    }
}
