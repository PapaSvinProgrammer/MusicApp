package com.example.musicapp.presintation.favorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.player.PlayerService

class FavoriteViewModel: ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlay: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService
    val isBound = MutableLiveData<Boolean>()

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