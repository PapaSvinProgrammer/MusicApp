package com.example.musicapp.presintation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.ControlPlayer
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll

class HomeViewModel(
    private val getMusicAll: GetMusicAll
): ViewModel() {
    private val getMusicLiveData = MutableLiveData<LiveData<ArrayList<Music>>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val getMusicResult: LiveData<LiveData<ArrayList<Music>>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    var lastDownloadArray = ArrayList<Music>()
    var lastPosition = 0
    var isPlay = false

    fun getMusic() {
        getMusicLiveData.value = getMusicAll.execute()
    }

    fun getMusicWithFilter() {
        //TODO
    }

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }
}