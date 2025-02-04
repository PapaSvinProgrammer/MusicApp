package com.example.musicapp.presentation.settings

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val saveDarkModeState: SaveDarkModeState,
    private val saveLoginState: SaveLoginState,
    private val getEmail: GetEmail,
    private val getDarkModeState: GetDarkModeState
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService
    val isBound = MutableLiveData<Boolean>()

    private val _getEmail = MutableLiveData<String>()
    private val _getDarkModeState = MutableLiveData<Boolean>()

    val getEmailResult: LiveData<String> = _getEmail
    val getDarkModeStateResult: LiveData<Boolean> = _getDarkModeState

    fun saveDarkMode(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDarkModeState.execute(state)
        }
    }

    fun saveLoginState() {
        viewModelScope.launch(Dispatchers.IO) {
            saveLoginState.execute(false)
        }
    }

    fun getEmail() {
        viewModelScope.launch {
            getEmail.execute().collect { result ->
                _getEmail.value = result
            }
        }
    }

    fun getDarkMode() {
        viewModelScope.launch {
            getDarkModeState.execute().collect { result ->
                _getDarkModeState.value = result
            }
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}