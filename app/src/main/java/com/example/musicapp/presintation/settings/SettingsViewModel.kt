package com.example.musicapp.presintation.settings

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val saveDarkModeState: SaveDarkModeState,
    private val saveLoginState: SaveLoginState
): ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlay: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService
    val isBound = MutableLiveData<Boolean>()

    fun saveDarkMode(state: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            saveDarkModeState.execute(state)
        }
    }

    fun saveLoginState() {
        saveLoginState.execute(false)
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