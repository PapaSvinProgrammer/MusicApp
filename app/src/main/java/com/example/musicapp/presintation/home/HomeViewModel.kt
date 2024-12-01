package com.example.musicapp.presintation.home

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
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMusicAll: GetMusicAll
): ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlayService: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    var lastDownloadArray = ArrayList<Music>()
    var lastPosition = 0

    fun getMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicAll.execute()
        }
    }

    fun getMusicWithFilter() {
        //TODO
    }

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            maxDurationLiveData = binder.getMaxDuration()
            durationLiveData = binder.getCurrentDuration()
            currentPosition = binder.getCurrentPosition()
            isPlayService = binder.isPlay()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}