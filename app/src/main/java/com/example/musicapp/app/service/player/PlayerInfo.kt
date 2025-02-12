package com.example.musicapp.app.service.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.Music

object PlayerInfo {
    private val _musicList = MutableLiveData<List<Music>>()
    private val _duration = MutableLiveData<Long>()
    private val _currentObject = MutableLiveData<Music>()
    private val _isPlay = MutableLiveData<Boolean>()
    private val _isFavorite = MutableLiveData<Boolean>()

    val musicList: LiveData<List<Music>> = _musicList
    val duration: LiveData<Long> = _duration
    val currentObject: LiveData<Music> = _currentObject
    val isPlay: LiveData<Boolean> = _isPlay
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun setMusicList(list: List<Music>) {
        _musicList.value = list
    }

    fun addItem(item: Music) {
        val currentList = _musicList.value?.toMutableList()
        currentList?.add(item)

        currentList?.let { _musicList.value = it }
    }

    fun addItem(position: Int, music: Music) {
        val currentList = _musicList.value?.toMutableList()
        currentList?.add(position, music)

        currentList?.let { _musicList.value = it }
    }

    fun setDuration(duration: Long) {
        _duration.value = duration
    }

    fun setCurrentObject(item: Music) {
        _currentObject.value = item
    }

    fun setIsPlay(isPlay: Boolean) {
        _isPlay.value = isPlay
    }

    fun setIsFavorite(state: Boolean) {
        _isFavorite.value = state
    }
}