package com.cooperthecoder.implant

import android.content.Context
import com.cooperthecoder.implant.data.DeviceProperties
import okhttp3.OkHttpClient

object Networking {

    private var client: OkHttpClient? = null

    fun httpClient(context: Context): OkHttpClient {
        if (client == null) {
            client = OkHttpClient.Builder()
                    .addInterceptor {
                        val original = it.request()
                        val request = original.newBuilder()
                                .addHeader("User-Agent", DeviceProperties.deviceId(context))
                                .build()

                        it.proceed(request)
                    }
                    .build()
        }
        return client!!
    }
}