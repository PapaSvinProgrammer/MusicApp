package com.example.musicapp.presentation.authorMusicList

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAuthorId
import com.example.musicapp.service.player.PlayerService
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val getMusicsByAuthorId: GetMusicsByAuthorId
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var playerService: PlayerService? = null
    var isBound = MutableLiveData<Boolean>()

    private val musicsLiveData = MutableLiveData<List<Music>>()
    val musicsResult: LiveData<List<Music>> = musicsLiveData

    fun getMusics(authorId: String) {
        viewModelScope.launch {
            musicsLiveData.value = getMusicsByAuthorId.execute(authorId)
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            playerService = binder.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}