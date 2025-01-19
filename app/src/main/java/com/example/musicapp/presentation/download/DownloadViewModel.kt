package com.example.musicapp.presentation.download

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.service.player.PlayerService
import kotlinx.coroutines.launch

private const val DOWNLOAD_LIMIT = 12

class DownloadViewModel(
    private val getDownloadedMusic: GetDownloadedMusic
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val musicLiveData = MutableLiveData<List<Music>>()
    val musicResult: LiveData<List<Music>> = musicLiveData

    fun getDownloadMusic() {
        viewModelScope.launch {
            musicLiveData.value = getDownloadedMusic.getDownloadsLimit(DOWNLOAD_LIMIT)
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