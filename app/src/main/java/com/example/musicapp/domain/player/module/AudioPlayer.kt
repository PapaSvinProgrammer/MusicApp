package com.example.musicapp.domain.player.module

import com.example.musicapp.domain.module.Music

interface AudioPlayer {
    fun play()
    fun pause()
    fun reset()
    fun repeat(state: Boolean)
    fun getCurrentDuration(): Long
    fun getMaxDuration():Long
    fun getBufferedPosition(): Long
    fun getCurrentItem(): Int
    fun seekTo(msec: Long)
    fun setList(list: List<Music>)
    fun setPosition(position: Int, isPlay: Boolean)
    fun shuffle()
}