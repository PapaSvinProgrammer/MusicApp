package com.example.musicapp.presentation.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetUserKey
import kotlinx.coroutines.launch

class MainViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val getUserKey: GetUserKey,
    private val getMusicAll: GetMusicAll
): ViewModel() {
    var durationLiveData: LiveData<Long>? = null
    var maxDurationLiveData: LiveData<Long>? = null
    var isPlayService: LiveData<Boolean>? = null
    var currentPosition: LiveData<Int>? = null
    var musicList: LiveData<List<Music>>? = null
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    var darkModeResult: Boolean = false
    var userKeyResult: String? = null
    var initSuccess: Boolean = false
    var initPlayerSuccess: Boolean = false

    private val startDownloadLiveData = MutableLiveData<Boolean>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()


    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val startDownloadResult: LiveData<Boolean> = startDownloadLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun getDarkMode() {
        darkModeResult = getDarkModeState.execute()
    }

    fun getUserKey() {
        userKeyResult = getUserKey.execute()
    }

    fun getMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicAll.execute()
        }
    }

    fun setStartState(state: Boolean) {
        startDownloadLiveData.value = state
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            maxDurationLiveData = binder.getMaxDuration()
            durationLiveData = binder.getCurrentDuration()
            currentPosition = binder.getCurrentPosition()
            isPlayService = binder.isPlay()
            musicList = binder.getMusicList()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}