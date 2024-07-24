package com.spin.wheel.chooser.wheeloffortune.network.interceptor

import android.text.TextUtils
import android.util.Log
import com.google.android.datatransport.BuildConfig
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePref
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val sharedPref: MySharePref) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val headers: Headers
        val accessToken = ""
        if (!TextUtils.isEmpty(accessToken)) {
            if (BuildConfig.DEBUG) {
                Log.v("OkHttp", "Token " + accessToken)
            }
            headers = Headers.Builder()
                .add(
                    "Authorization",
                    "Bearer " + accessToken
                )
                .add(
                    "key",
                    "hsba"
                )
                .build()
        } else {
            headers = Headers.Builder()
                .build()
        }

        val newRequest: Request =
            request.newBuilder()
                .headers(headers)
                .build()

        return chain.proceed(newRequest)
    }
}