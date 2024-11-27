package com.example.musicapp.domain.player.module

import com.example.musicapp.domain.module.Music

interface AudioPlayer {
    fun play()
    fun pause()
    fun addNewObjectAndStart(music: Music, isPlay: Boolean = false)
}