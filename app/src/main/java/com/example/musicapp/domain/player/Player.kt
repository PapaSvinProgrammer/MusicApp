package com.example.musicapp.domain.player

import android.media.MediaPlayer
import android.util.Log
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.module.AudioPlayer

class Player(
    private var mediaPlayer: MediaPlayer = MediaPlayer()
): AudioPlayer {
    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun reset() {
        mediaPlayer.seekTo(0)
    }

    override fun repeat(state: Boolean) {
        mediaPlayer.isLooping = state
    }

    override fun getCurrentDuration(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getMaxDuration(): Int {
        return mediaPlayer.duration
    }

    override fun addNewObjectAndStart(music: Music, isPlay: Boolean) {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()

        if (music.url.isEmpty()) return

        mediaPlayer.setDataSource(music.url)
        mediaPlayer.prepareAsync()

        if (!isPlay) return

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }

    override fun seekTo(msec: Int) {
        mediaPlayer.seekTo(msec)
    }
}