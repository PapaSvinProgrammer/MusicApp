package com.example.musicapp.domain.player.module

import com.example.musicapp.domain.module.Music

interface AudioPlayer {
    fun play()
    fun pause()
    fun getCurrentDuration(): Int
    fun getMaxDuration(): Int
    fun addNewObjectAndStart(music: Music, isPlay: Boolean = false)
    fun seekTo(msec: Int)
}