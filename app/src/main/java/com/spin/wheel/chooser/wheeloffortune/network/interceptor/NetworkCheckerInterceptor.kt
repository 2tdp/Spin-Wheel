package com.spin.wheel.chooser.wheeloffortune.network.interceptor

import android.content.Context
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkCheckerInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (DeviceUtil.hasConnection(context)) {
            chain.proceed(chain.request())
        } else {
            throw NoConnectivityException()
        }
    }

    inner class NoConnectivityException : Exception() {
        override val message: String?
            get() = "Không có kết nối mạng!"
    }
}
