package com.example.musicapp.app.service.player.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.Music

object PlayerInfo {
    private val isPlayLiveData = MutableLiveData<Boolean>()
    private val currentObjectLiveData = MutableLiveData<Music>()
    private var currentPositionLiveData = MutableLiveData<Int>()

    val isPlay: LiveData<Boolean> = isPlayLiveData
    val currentObject: LiveData<Music> = currentObjectLiveData
    val currentPosition: LiveData<Int> = currentPositionLiveData

    fun setCurrentObject(music: Music) {
        currentObjectLiveData.value = music
    }

    fun setIsPlay(state: Boolean) {
        isPlayLiveData.value = state
    }

    fun setCurrentPosition(position: Int) {
        currentPositionLiveData.value = position
    }
}