package com.cooperthecoder.implant

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.net.ConnectivityManagerCompat

object Networking {
    fun isUnmeteredNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ! ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)
    }

}