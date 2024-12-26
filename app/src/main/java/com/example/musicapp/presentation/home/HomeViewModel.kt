package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMusicAll: GetMusicAll
): ViewModel() {
    lateinit var isPlayService: LiveData<Boolean>
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()

    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun musicForSearch() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicAll.execute()
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            isPlayService = binder.isPlay()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}