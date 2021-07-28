package com.example.test.feature.arch

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class InterceptorFixer(private val parentInterceptor: Interceptor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return parentInterceptor.intercept(chain)
        } catch (t: Throwable) {
            throw if (t is IOException) t else IOException(t)
        }
    }
}
