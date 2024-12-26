package com.example.musicapp.domain.player.module

import com.example.musicapp.domain.module.Music

interface AudioPlayer {
    fun play()
    fun pause()
    fun reset()
    fun repeat(state: Boolean)
    fun getCurrentDuration(): Long
    fun getMaxDuration():Long
    fun addNewObjectAndStart(music: Music, isPlay: Boolean = false)
    fun seekTo(msec: Long)
}