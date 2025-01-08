package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

class PlaylistFavoriteViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    var currentPosition: LiveData<Int>? = null
    var currentObject: LiveData<Music>? = null
    var isPlay: LiveData<Boolean>? = null
    val isBound = MutableLiveData<Boolean>()

    private val getPlaylistLiveData = MutableLiveData<PlaylistResult?>()
    val getPlaylistResult: LiveData<PlaylistResult?> = getPlaylistLiveData

    fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.getById(1)
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            currentPosition = binder.getCurrentPosition()
            currentObject = binder.getCurrentObject()
            isPlay = binder.isPlay()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}