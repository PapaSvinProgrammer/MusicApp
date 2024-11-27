package com.example.musicapp.presintation.mainPlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.player.state.ControlPlayer
import com.example.musicapp.domain.player.state.StatePlayer

class PlayerViewModel: ViewModel() {
    private val controlPlayerLiveData = MutableLiveData<ControlPlayer>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val controlPlayer: LiveData<ControlPlayer> = controlPlayerLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    var lastPosition = 0

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun setControlPlayer(state: ControlPlayer) {
        controlPlayerLiveData.value = state
    }
}