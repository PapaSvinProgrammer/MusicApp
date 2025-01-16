package com.example.musicapp.service.video

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.domain.module.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoService: Service() {
    private val isSuccess = MutableLiveData<Boolean>()

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
        fun getService() = this@VideoService
        fun isSuccess() = this@VideoService.isSuccess
    }

    override fun onDestroy() {
        VideoPlayer.exoPlayer?.release()
        VideoPlayer.exoPlayer = null

        super.onDestroy()
    }

    fun setVideo(music: Music, isPlay: Boolean) {
        if (music.movieUrl.isNullOrEmpty()) {
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)

            val mediaItem = MediaItem.fromUri(music.movieUrl.toString())
            VideoPlayer.exoPlayer?.addMediaItem(mediaItem)
            VideoPlayer.exoPlayer?.prepare()

            delay(2000)

            isSuccess.value = true
            VideoPlayer.exoPlayer?.playWhenReady = isPlay
        }
    }

    fun pause() {
        VideoPlayer.exoPlayer?.pause()
    }

    fun play() {
        VideoPlayer.exoPlayer?.play()
    }

    fun reset() {
        VideoPlayer.exoPlayer?.clearMediaItems()
    }

    private fun initPlayer() {
        VideoPlayer.exoPlayer = ExoPlayer.Builder(this@VideoService).build()
        VideoPlayer.exoPlayer?.playWhenReady = true
        VideoPlayer.exoPlayer?.volume = 0F
    }
}