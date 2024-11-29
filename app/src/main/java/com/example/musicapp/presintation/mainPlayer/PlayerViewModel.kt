package com.example.musicapp.presintation.mainPlayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.ControlPlayer
import com.example.musicapp.domain.player.state.StatePlayer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PlayerViewModel: ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlay: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService

    val isBound = MutableLiveData<Boolean>()
    private val controlPlayerLiveData = MutableLiveData<ControlPlayer>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val missTimeLiveData = MutableLiveData<String>()
    private val passTimeLiveData = MutableLiveData<String>()

    val controlPlayer: LiveData<ControlPlayer> = controlPlayerLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val missTimeResult: LiveData<String> = missTimeLiveData
    val passTimeResult: LiveData<String> = passTimeLiveData

    var lastPosition = 0

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun setControlPlayer(state: ControlPlayer) {
        controlPlayerLiveData.value = state
    }

    fun seekTo(msec: Int?) {
        if (msec != null) servicePlayer.seekTo(msec)
    }

    @SuppressLint("SimpleDateFormat")
    fun getMissTime(current: Int?) {
        viewModelScope.launch {
            if (current == null) return@launch
            val result = (maxDurationLiveData.value ?: 0) - current

            val simpleDateFormat = SimpleDateFormat("m:ss")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = result.toLong()

            missTimeLiveData.value = simpleDateFormat.format(calendar.time)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getPassTime(current: Int) {
        viewModelScope.launch {
            val simpleDateFormat = SimpleDateFormat("m:ss")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = current.toLong()

            passTimeLiveData.value = simpleDateFormat.format(calendar.time)
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            maxDurationLiveData = bind.getMaxDuration()
            durationLiveData = bind.getCurrentDuration()
            isPlay = bind.isPlay()
            currentPosition = bind.getCurrentPosition()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}