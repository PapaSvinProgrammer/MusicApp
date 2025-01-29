package com.example.musicapp.app.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkReceiver: BroadcastReceiver() {
    companion object {
        const val WIFI_FILTER = WifiManager.WIFI_STATE_CHANGED_ACTION
    }

    private var callback: ((Int) -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectState = intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)
        callback?.invoke(connectState ?: -1)
    }

    fun setCallback(callback: (Int) -> Unit) {
        this.callback = callback
    }
}