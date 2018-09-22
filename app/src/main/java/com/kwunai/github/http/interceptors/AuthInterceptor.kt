package com.kwunai.github.http.interceptors

import android.util.Base64
import com.kwunai.github.data.PrefsHelper
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
        private val helper: PrefsHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return chain.proceed(original.newBuilder().apply {
            when {
                original.url().pathSegments().contains("authorizations") -> {
                    val auth = "${helper.username}:${helper.password}".let {
                        "Basic ${Base64.encodeToString(it.toByteArray(), Base64.DEFAULT).trim()}"
                    }
                    header("Authorization", auth)
                }
                helper.isLoggedIn() -> {
                    val auth = "Token ${helper.token}"
                    header("Authorization", auth)
                }
                else -> removeHeader("Authorization")
            }
        }.build())
    }

}