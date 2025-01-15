package com.example.musicapp.service.video

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.domain.module.Music

class VideoService: Service() {
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("RRRR", "CREATE VIDEO SERVICE")

        initPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        return VideoBinder()
    }

    inner class VideoBinder: Binder() {
        fun getExoPlayer() = this@VideoService.exoPlayer
        fun getService() = this@VideoService
    }

    override fun onDestroy() {
        Log.d("RRRR", "DESTROY VIDEO SERVICE")
        exoPlayer?.release()

        super.onDestroy()
    }

    fun setVideo(music: Music) {

    }

    private fun initPlayer() {
        exoPlayer = ExoPlayer.Builder(this@VideoService).build()
    }
}