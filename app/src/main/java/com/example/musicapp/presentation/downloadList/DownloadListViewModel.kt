package com.example.musicapp.presentation.downloadList

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
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchDownloadedLocal
import com.example.musicapp.service.player.PlayerService
import kotlinx.coroutines.launch

class DownloadListViewModel(
    private val getDownloadedMusic: GetDownloadedMusic,
    private val searchDownloadedLocal: SearchDownloadedLocal
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    var isPlay: LiveData<Boolean>? = null
    var currentObject: LiveData<Music>? = null
    val isBound = MutableLiveData<Boolean>()

    private val musicLiveData = MutableLiveData<List<Music>>()
    private val searchLiveData = MutableLiveData<List<Music>>()

    val musicResult: LiveData<List<Music>> = musicLiveData
    val searchResult: LiveData<List<Music>> = searchLiveData

    fun getDownloadedMusic() {
        viewModelScope.launch {
            musicLiveData.value = getDownloadedMusic.getDownloads()
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchDownloadedLocal.execute(text)
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            isPlay = bind.isPlay()
            currentObject = bind.getCurrentObject()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}