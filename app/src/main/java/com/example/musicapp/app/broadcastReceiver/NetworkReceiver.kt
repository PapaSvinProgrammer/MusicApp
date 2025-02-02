package com.example.musicapp.app.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkReceiver: BroadcastReceiver() {
    private var callback: ((Int?) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val conn = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        callback?.invoke(networkInfo?.type)
    }

    fun setCallback(callback: (Int?) -> Unit) {
        this.callback = callback
    }
}