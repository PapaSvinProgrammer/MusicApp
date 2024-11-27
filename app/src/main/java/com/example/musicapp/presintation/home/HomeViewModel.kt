package com.example.musicapp.presintation.home

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll

class HomeViewModel(
    private val getMusicAll: GetMusicAll
): ViewModel() {
    lateinit var durationLiveData: LiveData<Float>
    lateinit var maxDurationLiveData: LiveData<Float>
    lateinit var isPlayService: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService
    val isBound = MutableLiveData<Boolean>()

    private val getMusicLiveData = MutableLiveData<LiveData<ArrayList<Music>>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val getMusicResult: LiveData<LiveData<ArrayList<Music>>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    var lastDownloadArray = ArrayList<Music>()
    var lastPosition = 0

    fun getMusic() {
        getMusicLiveData.value = getMusicAll.execute()
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