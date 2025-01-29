package com.example.musicapp.app.service.player.module

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
    fun setData(list: List<Music>)
    fun setDataDownload(list: List<Music>)
    fun setPosition(position: Int, isPlay: Boolean)
    fun shuffle()
    fun addMusic(music: Music)
    fun addInQueue(music: Music)
    fun addNext(music: Music)
    fun release()
}