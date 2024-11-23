package com.example.musicapp.domain.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.Music

class PlayerService: Service() {
    private var musicList: List<Music>? = null
    private var mediaPlayer = MediaPlayer()
    private val currentDuration = MutableLiveData<Float>()
    private val maxDuration = MutableLiveData<Float>()
    private var isPlay = MutableLiveData<Boolean>()
    private var lastPosition = MutableLiveData<Int>()

    override fun onBind(intent: Intent?): IBinder {
        return PlayerBinder()
    }

    inner class PlayerBinder: Binder() {
        fun getService(): PlayerService = this@PlayerService

        fun setMusicList(list: List<Music>) {
            musicList = list
        }

        fun getCurrentDuration() = this@PlayerService.currentDuration

        fun getMaxDuration() = this@PlayerService.maxDuration

        fun isPlay() = this@PlayerService.isPlay

        fun getLastPosition() = this@PlayerService.lastPosition
    }

    fun setPlayerState(state: StatePlayer) {
        when (state) {
            StatePlayer.PLAY -> play()
            StatePlayer.PAUSE -> pause()
            StatePlayer.PREVIOUS -> previous()
            StatePlayer.NEXT -> next()
            StatePlayer.NONE -> {}
        }
    }

    private fun pause() {
        isPlay.value = false
        Log.d("RRRR", "Pause")
    }

    private fun play() {
        isPlay.value = true
        Log.d("RRRR", "Play")
    }

    private fun previous() {
        (lastPosition.value ?: 0) - 1
        Log.d("RRRR", "prev")
    }

    private fun next() {
        (lastPosition.value ?: 0) + 1
        Log.d("RRRR", "next")
    }
}